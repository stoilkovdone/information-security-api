package mk.ukim.finki.informationsecurityapi.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.informationsecurityapi.api.dto.UserLoginDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserRegisterDTO;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.exception.BadCredentialsException;
import mk.ukim.finki.informationsecurityapi.exception.ResourceNotFoundException;
import mk.ukim.finki.informationsecurityapi.repository.UserRepository;
import mk.ukim.finki.informationsecurityapi.service.AuthService;
import mk.ukim.finki.informationsecurityapi.service.helper.PasswordHasher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        validateUserRegistration(userRegisterDTO);

        User user = new User();

        user.setUsername(userRegisterDTO.username());
        user.setEmail(userRegisterDTO.email());
        user.setPassword(PasswordHasher.hashPassword(userRegisterDTO.password()));

        userRepository.save(user);
    }

    @Override
    public void login(UserLoginDTO userLoginDTO, HttpServletResponse response) {
        Optional<User> optionalUser = userRepository.findByEmail(userLoginDTO.email());
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException(String.format("User with email: %s not found", userLoginDTO.email()));
        }

        User user = optionalUser.get();
        if (!PasswordHasher.verifyPassword(userLoginDTO.password(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        String sessionToken = UUID.randomUUID().toString();

        Cookie cookie = new Cookie("SESSION_ID", sessionToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
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
}
