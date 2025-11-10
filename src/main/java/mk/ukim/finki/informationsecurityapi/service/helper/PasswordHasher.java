package mk.ukim.finki.informationsecurityapi.service.helper;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {
    private static final SecureRandom random = new SecureRandom();

    public static String hashPassword(String password) {
        String salt = generateSalt();
        String hashedPassword = hash(password, salt);
        return salt + ":" + hashedPassword;
    }

    public static boolean verifyPassword(String password, String storedHash) {
        String salt = storedHash.split(":")[0];
        String storedHashedPassword = storedHash.split(":")[1];
        String hashedPassword = hash(password, salt);
        return hashedPassword.equals(storedHashedPassword);
    }

    private static String generateSalt() {
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    private static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest((salt + password).getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
