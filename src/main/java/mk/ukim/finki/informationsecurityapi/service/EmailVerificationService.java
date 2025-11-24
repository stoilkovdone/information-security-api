package mk.ukim.finki.informationsecurityapi.service;

import mk.ukim.finki.informationsecurityapi.domain.User;

public interface EmailVerificationService {

    String createVerificationToken(User user);

    void verifyEmail(String token);

}
