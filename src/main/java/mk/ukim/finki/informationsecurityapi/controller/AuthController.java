package mk.ukim.finki.informationsecurityapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mk.ukim.finki.informationsecurityapi.api.dto.UserLoginDTO;
import mk.ukim.finki.informationsecurityapi.api.dto.UserRegisterDTO;
import mk.ukim.finki.informationsecurityapi.domain.User;
import mk.ukim.finki.informationsecurityapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AuthController.BASE_URL)
@CrossOrigin(allowedHeaders = "*", origins = "*")
@AllArgsConstructor
public class AuthController {

    public static final String BASE_URL="/api/v1/auth";

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserRegisterDTO userRegisterDTO) {
        authService.register(userRegisterDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        authService.login(userLoginDTO, response);
    }

    @PostMapping("/verify-email")
    @ResponseStatus(HttpStatus.OK)
    public void verifyEmail(@RequestParam String verificationToken) {
        authService.verifyEmail(verificationToken);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> retrieveAllUsers(HttpServletRequest request){
        return authService.getAllUsers(request);
    }

}
