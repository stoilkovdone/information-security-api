package mk.ukim.finki.informationsecurityapi.service;

import mk.ukim.finki.informationsecurityapi.domain.User;

public interface OTPVerificationService {

    String createOTP(User user);

    void verifyOTP(User user, String otp);

}
