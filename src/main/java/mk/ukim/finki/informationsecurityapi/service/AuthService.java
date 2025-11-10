package mk.ukim.finki.informationsecurityapi.service;

import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.informationsecurityapi.api.dto.UserLoginDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserRegisterDTO;

public interface AuthService {

    void register(UserRegisterDTO userRegisterDTO);

    void login(UserLoginDTO userLoginDTO, HttpServletResponse response);

}
