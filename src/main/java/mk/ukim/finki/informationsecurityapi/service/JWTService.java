package mk.ukim.finki.informationsecurityapi.service;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.informationsecurityapi.domain.User;

public interface JWTService {

    String generateToken(User user);

    void validateRequest(HttpServletRequest request);

}
