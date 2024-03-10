package com.matchhub.matchhub.security.auth;

import com.matchhub.matchhub.security.dto.AuthResponseDTO;
import com.matchhub.matchhub.security.dto.LoginDTO;
import com.matchhub.matchhub.security.dto.SingUpDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Tag(name = "Authentication", description = "")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //SingUp
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody SingUpDTO request) {
        return ResponseEntity.ok(service.register(request));
    }

    //LogIn
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
}
