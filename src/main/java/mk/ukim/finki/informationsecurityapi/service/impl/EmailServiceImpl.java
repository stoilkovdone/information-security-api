package mk.ukim.finki.informationsecurityapi.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.informationsecurityapi.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(String toEmail, String username, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify Your Email Address");

            String verificationLink = frontendUrl + "/verify-email?token=" + token;

            String htmlContent = buildVerificationEmail(username, verificationLink);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendOTP(String toEmail, String username, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Verify One Time Password");

            String htmlContent = buildOtpEmail(username, otp);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send one time password email", e);
        }
    }


    private String buildVerificationEmail(String username, String verificationLink) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .button {
                        display: inline-block;
                        padding: 12px 24px;
                        background-color: #007bff;
                        color: white;
                        text-decoration: none;
                        border-radius: 4px;
                        margin: 20px 0;
                    }
                    .code {
                        font-size: 24px;
                        font-weight: bold;
                        letter-spacing: 2px;
                        color: #007bff;
                        padding: 10px;
                        background-color: #f5f5f5;
                        border-radius: 4px;
                        display: inline-block;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2>Welcome, %s!</h2>
                    <p>Thank you for registering. Please verify your email address to complete your registration.</p>
            
                    <p>Click the button below:</p>
                    <a href="%s" class="button">Verify Email Address</a>
            
                    <p>This link will expire in 24 hours.</p>
            
                    <p>If you didn't create an account, please ignore this email.</p>
            
                    <hr>
                    <p style="font-size: 12px; color: #666;">
                        If the button doesn't work, copy and paste this link into your browser:<br>
                        %s
                    </p>
                </div>
            </body>
            </html>
            """.formatted(username, verificationLink, verificationLink);
    }

    private String buildOtpEmail(String username, String otp) {
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                .otp-box {
                    text-align: center;
                    margin: 30px 0;
                    padding: 20px;
                    background-color: #f8f9fa;
                    border-radius: 8px;
                    border: 2px dashed #007bff;
                }
                .otp-code {
                    font-size: 32px;
                    font-weight: bold;
                    letter-spacing: 8px;
                    color: #007bff;
                    padding: 15px;
                    background-color: #ffffff;
                    border-radius: 4px;
                    display: inline-block;
                    margin: 10px 0;
                }
                .warning {
                    background-color: #fff3cd;
                    border-left: 4px solid #ffc107;
                    padding: 12px;
                    margin: 20px 0;
                    border-radius: 4px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h2>Hello, %s!</h2>
                <p>You have requested a one-time password (OTP) to verify your identity.</p>
        
                <div class="otp-box">
                    <p style="margin: 0; font-size: 14px; color: #666;">Your verification code is:</p>
                    <div class="otp-code">%s</div>
                    <p style="margin: 0; font-size: 12px; color: #666;">This code will expire in 10 minutes</p>
                </div>
        
                <p>Enter this code in the application to complete your verification.</p>
        
                <div class="warning">
                    <strong>⚠️ Security Notice:</strong><br>
                    Never share this code with anyone. Our team will never ask for your verification code.
                </div>
        
                <p>If you didn't request this code, please ignore this email or contact our support team.</p>
        
                <hr>
                <p style="font-size: 12px; color: #666;">
                    This is an automated message, please do not reply to this email.
                </p>
            </div>
        </body>
        </html>
        """.formatted(username, otp);
    }
}
