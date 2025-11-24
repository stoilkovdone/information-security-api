package mk.ukim.finki.informationsecurityapi.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.informationsecurityapi.api.dto.UserLoginDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserRegisterDTO;
import mk.ukim.finki.informationsecurityapi.domain.User;

import java.util.List;

public interface AuthService {

    List<User> getAllUsers(HttpServletRequest request);

    void register(UserRegisterDTO userRegisterDTO);

    void login(UserLoginDTO userLoginDTO, HttpServletResponse response);

    void verifyEmail(String verificationToken);

}
