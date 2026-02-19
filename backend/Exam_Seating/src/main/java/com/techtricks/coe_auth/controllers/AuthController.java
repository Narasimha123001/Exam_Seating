package com.techtricks.coe_auth.controllers;
import com.techtricks.coe_auth.dtos.LoginRequest;
import com.techtricks.coe_auth.dtos.UserDto;
import com.techtricks.coe_auth.dtos.UserResponseDto;
import com.techtricks.coe_auth.exceptions.UserAlreadyPresentException;
import com.techtricks.coe_auth.models.User;
import com.techtricks.coe_auth.services.UserService;
import com.techtricks.coe_auth.utils.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDto  user) {
        try {
            UserResponseDto saved = userService.saveUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(saved);
        }catch (UserAlreadyPresentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("User already present ", e.getMessage()));
        }
    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid @RequestBody LoginRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User dbUser = userService.getUserByEmail(request.getEmail());

            return jwtUtil.generateToken(dbUser.getEmail() , dbUser.getRole().name() , dbUser.getRegisterNumber());
        }catch(AuthenticationException e){
            throw new RuntimeException("Invalid username or password");
        }
    }
}
