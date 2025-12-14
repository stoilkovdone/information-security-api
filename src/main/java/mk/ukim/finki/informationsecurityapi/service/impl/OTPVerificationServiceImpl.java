package mk.ukim.finki.informationsecurityapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.informationsecurityapi.domain.OTPVerification;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.exception.BadCredentialsException;
import mk.ukim.finki.informationsecurityapi.repository.OTPVerificationRepository;
import mk.ukim.finki.informationsecurityapi.service.OTPVerificationService;
import mk.ukim.finki.informationsecurityapi.service.helper.PasswordHasher;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OTPVerificationServiceImpl implements OTPVerificationService {

    private final OTPVerificationRepository otpVerificationRepository;

    @Override
    public String createOTP(User user) {
        OTPVerification otpVerification = new OTPVerification();

        otpVerification.setUser(user);
        otpVerification.setValidUntil(LocalDateTime.now().plusMinutes(10));

        String otp = generateOTP();
        String hashedOTP = PasswordHasher.hashPassword(otp);
        otpVerification.setOtp(hashedOTP);

        otpVerificationRepository.save(otpVerification);

        return otp;
    }

    @Override
    public void verifyOTP(User user, String otp) {
        Optional<OTPVerification> optionalOTPVerification =
                otpVerificationRepository.findByUserAndUsed(user, false);

        if (optionalOTPVerification.isEmpty()) {
            throw new BadCredentialsException("Invalid OTP");
        }

        OTPVerification otpVerification = optionalOTPVerification.get();

        if (!PasswordHasher.verifyPassword(otp, otpVerification.getOtp())) {
            throw new BadCredentialsException("Invalid OTP");
        }

        if (otpVerification.getValidUntil().isBefore(LocalDateTime.now())) {
            otpVerification.setUsed(true);
            otpVerificationRepository.save(otpVerification);
            throw new BadCredentialsException("OTP expired, please login again");
        }

        otpVerification.setUsed(true);

        otpVerificationRepository.save(otpVerification);
    }

    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 10000000 + random.nextInt(90000000);
        return String.valueOf(otp);
    }
}
