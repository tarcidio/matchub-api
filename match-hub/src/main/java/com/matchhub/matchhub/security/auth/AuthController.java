package com.matchhub.matchhub.security.auth;

import com.matchhub.matchhub.security.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@Tag(name = "Authentication", description = "")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //SignUp
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody SignUpDTO request, HttpServletResponse response) throws MessagingException, GeneralSecurityException, IOException {
        service.createUserAndSendEmailToRegister(request, response);
        return ResponseEntity.ok().build();
    }

    //LogIn
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody LoginDTO request, HttpServletResponse response) {
        return ResponseEntity.ok(service.authenticate(request, response));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordDTO forgotDTO) throws MessagingException, IOException, GeneralSecurityException {
        service.forgotPassword(forgotDTO);
        return ResponseEntity.ok().build();
    }


}
