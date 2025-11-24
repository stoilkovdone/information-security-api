package mk.ukim.finki.informationsecurityapi.service;

public interface EmailService {

    void sendVerificationEmail(String toEmail, String username, String token);

}
