package com.shashwat.ledger.controller;

import com.shashwat.ledger.dto.*;
import com.shashwat.ledger.security.JwtService;
import com.shashwat.ledger.service.AuthService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,AuthService authService){
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.authService=authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){

        try {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            System.out.println(encoder.encode("admin123"));
            System.out.println(
                    encoder.matches(
                            request.getPassword(),
                            "$2a$10$9Dk9C0rK1W3TQe8dR3kYxO0vFq6qVbJ9Xj0r1g6l4x5P8m2u7zKjS"
                    )
            );

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(request.getEmail());

        return new AuthResponse(token);
    }
    @PostMapping("/create-user")
    public ApiResponse<Void> createUser(
            @RequestBody CreateUserRequest request) {

        authService.createUser(request);

        return ApiResponse.<Void>builder()
                .data(null)
                .message("User created successfully")
                .status(200)
                .build();
    }
}