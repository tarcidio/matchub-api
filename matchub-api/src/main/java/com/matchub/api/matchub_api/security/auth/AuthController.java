package com.matchub.api.matchub_api.security.auth;

import com.matchub.api.matchub_api.security.dto.AuthResponseDTO;
import com.matchub.api.matchub_api.security.dto.ForgotPasswordDTO;
import com.matchub.api.matchub_api.security.dto.LoginDTO;
import com.matchub.api.matchub_api.security.dto.SignUpDTO;
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
    /*
    To register, the user must confirm their email. For this, this route sends a link to the provided email.
    Through this link, the user accesses a page that automatically validates the email in the backend.
    This validation occurs because a JWT token is passed in the URL. It could be done differently,
    by forcing the user to enter a code, but this would require creating a new table in the database and
    a new logic (Service) for this code, which is not worthwhile since the JWT, besides offering equivalent security,
    already has a structure.
    * */
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
