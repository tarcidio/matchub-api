package com.matchhub.matchhub.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.security.cookie.CookieService;
import com.matchhub.matchhub.security.token.domain.enums.Role;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.security.dto.AuthResponseDTO;
import com.matchhub.matchhub.security.dto.ForgotPasswordDTO;
import com.matchhub.matchhub.security.dto.LoginDTO;
import com.matchhub.matchhub.security.dto.SignUpDTO;
import com.matchhub.matchhub.security.email.EmailService;
import com.matchhub.matchhub.security.jwt.JwtService;
import com.matchhub.matchhub.security.token.service.TokenService;
import com.matchhub.matchhub.service.HubUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final HubUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final HubUserService hubUserService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final CookieService cookieService;

    private String generateRevokeAndSaveAccessToken(HubUser hubUser){
        // Generate tokens
        String jwtToken = tokenService.generateToken(hubUser);
        // Delete all tokens from hubUser
        tokenService.revokeAllUserTokens(hubUser);
        // Salve tokens
        tokenService.saveUserToken(hubUser, jwtToken);
        // Return token
        return jwtToken;
    }

    private AuthResponseDTO manageTokens(HubUser hubUser, HttpServletResponse response){
        String jwtToken = generateRevokeAndSaveAccessToken(hubUser);
        String refreshToken = tokenService.generateRefreshToken(hubUser);

        // Put refreshToken in Cookies
        cookieService.addCookie(response, refreshToken);

        // Return response
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .build();
    }

    //SignUp
    public void createUserAndSendEmailToRegister(SignUpDTO request, HttpServletResponse response) throws MessagingException, GeneralSecurityException, IOException {
        // Transfer information
        HubUser newHubUser = modelMapper.map(request, HubUser.class);
        // Set id null (good practice)
        newHubUser.setId(null);
        // Encode password
        newHubUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save user
        HubUser savedHubUser = repository.save(newHubUser);
        // Set image default
        hubUserService.uploadDefaultImageS3(savedHubUser.getEmail());
        // Revoke, Generate and Save tokens
        String jwtToken = generateRevokeAndSaveAccessToken(savedHubUser);
        // Send link to check email
        emailService.sendCheckEmail(savedHubUser.getEmail(), jwtToken);
    }

    //Login
    public AuthResponseDTO authenticate(LoginDTO request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        HubUser hubUser = repository.findByUsername(request.getUsername())
                .orElseThrow();
        // Verificação do papel do usuário
        checkUserRole(hubUser);
        return manageTokens(hubUser, response);
    }

    private void checkUserRole(HubUser hubUser) {
        if (hubUser.getRole().equals(Role.GUEST)) {
            throw new InsufficientAuthenticationException("Email verification is required for role: GUEST");
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String hubUserUsername;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
        refreshToken = cookieService.getRefreshTokenFromCookie(request);
        if(refreshToken == null)
            System.out.println("Cookie eh Nulo");
        hubUserUsername = tokenService.extractUsername(refreshToken);
        if (hubUserUsername != null) {
            HubUser hubUser = this.repository.findByUsername(hubUserUsername)
                    .orElseThrow();
            if (tokenService.isTokenValid(refreshToken, hubUser)) {
                String accessToken = tokenService.generateToken(hubUser);
                tokenService.revokeAllUserTokens(hubUser);
                tokenService.saveUserToken(hubUser, accessToken);
                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                        .accessToken(accessToken)
                        .build();
                // This object constructs a json manually and returns
                // Unfortunately, the object needs a supplement to deal with LocalDateTime
                // Then "registerModule(new JavaTimeModule())" must be added
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public void forgotPassword(ForgotPasswordDTO forgotDTO) throws MessagingException, IOException, GeneralSecurityException {
        String hubUserEmail = forgotDTO.getEmail();
        if (!isValidEmail(hubUserEmail)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        HubUser hubUser = repository.findByEmail(hubUserEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + hubUserEmail));
        String jwtToken = tokenService.generateToken(hubUser);
        // Delete all tokens from hubUser
        tokenService.revokeAllUserTokens(hubUser);
        // Salve tokens
        tokenService.saveUserToken(hubUser, jwtToken);
        emailService.sendRecoveryEmail(hubUserEmail, jwtToken);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }


}

// Invalidar o jwt token
// Limpar ou invalidar o cookies