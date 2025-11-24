package mk.ukim.finki.informationsecurityapi.service.impl;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.informationsecurityapi.domain.EmailVerificationToken;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.exception.ResourceNotFoundException;
import mk.ukim.finki.informationsecurityapi.repository.EmailVerificationTokenRepository;
import mk.ukim.finki.informationsecurityapi.service.EmailVerificationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;

    @Override
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        tokenRepository.save(verificationToken);

        return token;
    }

    @Override
    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new IllegalArgumentException("This verification token has already been used");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);

        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);
    }
}
