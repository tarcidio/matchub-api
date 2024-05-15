package com.matchhub.matchhub.security.auth;

import com.matchhub.matchhub.security.dto.AuthResponseDTO;
import com.matchhub.matchhub.security.dto.LoginDTO;
import com.matchhub.matchhub.security.dto.SignUpDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Tag(name = "Authentication", description = "")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //SignUp
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody SignUpDTO request, HttpServletResponse response) {
        return ResponseEntity.ok(service.register(request, response));
    }

    //LogIn
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody LoginDTO request, HttpServletResponse response) {
        return ResponseEntity.ok(service.authenticate(request, response));
    }

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }
}
