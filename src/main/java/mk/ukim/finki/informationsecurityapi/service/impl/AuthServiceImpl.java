package mk.ukim.finki.informationsecurityapi.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.informationsecurityapi.api.dto.SessionDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserLoginDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserRegisterDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.VerifyOtpDTO;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.exception.BadCredentialsException;
import mk.ukim.finki.informationsecurityapi.exception.ResourceNotFoundException;
import mk.ukim.finki.informationsecurityapi.repository.UserRepository;
import mk.ukim.finki.informationsecurityapi.service.*;
import mk.ukim.finki.informationsecurityapi.service.helper.AuthenticationHelper;
import mk.ukim.finki.informationsecurityapi.service.helper.PasswordHasher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final SessionService sessionService;
    private final EmailVerificationService emailVerificationService;
    private final OTPVerificationService otpVerificationService;
    private final EmailService emailService;

    @Override
    public List<User> getAllUsers(HttpServletRequest request) {
        AuthenticationHelper.authenticateRequest(request);

        return userRepository.findAll();
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        validateUserRegistration(userRegisterDTO);

        User user = new User();

        user.setUsername(userRegisterDTO.username());
        user.setEmail(userRegisterDTO.email());
        user.setPassword(PasswordHasher.hashPassword(userRegisterDTO.password()));
        user.setEmailVerified(false);

        userRepository.save(user);

        String token = emailVerificationService.createVerificationToken(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), token);
    }

    @Override
    public void login(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        User user = findUserByEmailOrThrow(userLoginDTO.email());

        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Please verify your email before logging in");
        }

        if (!PasswordHasher.verifyPassword(userLoginDTO.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String otp = otpVerificationService.createOTP(user);
        emailService.sendOTP(user.getEmail(), user.getUsername(), otp);
    }

    @Override
    public void verifyOTP(VerifyOtpDTO verifyOtpDTO, HttpServletResponse response) {
        User user = findUserByEmailOrThrow(verifyOtpDTO.email());
        otpVerificationService.verifyOTP(user, verifyOtpDTO.otp());


        SessionDTO session = sessionService.createSessionForUser(user);

        ResponseCookie cookie = ResponseCookie
                .from("session_id", session.token())
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(session.validForHours())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void logout(HttpServletRequest request) {
        String sessionId = AuthenticationHelper.getSessionId(request.getCookies());

        if (sessionId != null) {
            AuthenticationHelper.removeSession(sessionId);
        }
    }

    @Override
    public void verifyEmail(String verificationToken) {
        emailVerificationService.verifyEmail(verificationToken);
    }

    private void validateUserRegistration(UserRegisterDTO userRegisterDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userRegisterDTO.email());
        if (optionalUser.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (userRegisterDTO.password().trim().length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }

        Pattern pattern = Pattern.compile(
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9])\\S+$"
        );

        if (!pattern.matcher(userRegisterDTO.password()).matches()) {
            throw new IllegalArgumentException("Password not strong enough");
        }
    }

    private User findUserByEmailOrThrow(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with email: %s not found", email));
        }

        return optionalUser.get();
    }
}
