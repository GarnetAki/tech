package ru.soloviev.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.soloviev.Dao.CatDao;
import ru.soloviev.Dao.UserDao;
import ru.soloviev.Exchanges.LoginRequest;
import ru.soloviev.Dto.UserDto;
import ru.soloviev.Exchanges.RegisterRequest;
import ru.soloviev.Exchanges.StringResponse;
import ru.soloviev.Models.Role;
import ru.soloviev.Security.JWT.JwtUtils;
import ru.soloviev.Services.AuthService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@ComponentScan
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthService authService;

    private UserController userController;

    @Autowired
    public AuthController(UserDao userDao, CatDao catDao){
        this.userController = new UserController(userDao, catDao, authService, jwtUtils, encoder);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        UserDto userDto = authService.login(loginRequest);

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDto);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (authService.existsByLogin(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new StringResponse("Error: Username is already taken!"));
        }

        UserDto user = new UserDto();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setName(registerRequest.getName());
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setRole(Role.USER);

        if (Objects.equals(registerRequest.getAdminAccess(), "YaAdminOtvechayu"))
            user.setRole(Role.ADMIN);

        userController.userCreateSubmit(user);

        return ResponseEntity.ok(new StringResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new StringResponse("You've been signed out!"));
    }
}
