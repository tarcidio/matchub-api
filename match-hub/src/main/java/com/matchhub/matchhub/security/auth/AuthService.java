package com.matchhub.matchhub.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Token;
import com.matchhub.matchhub.domain.enums.TokenType;
import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.repository.TokenRepository;
import com.matchhub.matchhub.security.dto.AuthResponseDTO;
import com.matchhub.matchhub.security.dto.LoginDTO;
import com.matchhub.matchhub.security.dto.SingUpDTO;
import com.matchhub.matchhub.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final HubUserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private void saveUserToken(HubUser hubUser, String jwtToken) {
        Token token = Token.builder()
                .hubUser(hubUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    //SingUp
    public AuthResponseDTO register(SingUpDTO request) {
        // Transfer information
        HubUser newHubUser = modelMapper.map(request, HubUser.class);
        // Set id null (good practice)
        newHubUser.setId(null);
        // Encode password
        newHubUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save user
        HubUser savedHubUser = repository.save(newHubUser);
        // Generate tokens
        String jwtToken = jwtService.generateToken(newHubUser);
        String refreshToken = jwtService.generateRefreshToken(newHubUser);
        // Salve tokens
        saveUserToken(savedHubUser, jwtToken);
        // Give tokens to new user
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(HubUser hubUser) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(hubUser.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    //Login
    public AuthResponseDTO authenticate(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        HubUser hubUser = repository.findByUsername(request.getUsername())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(hubUser);
        String refreshToken = jwtService.generateRefreshToken(hubUser);
        revokeAllUserTokens(hubUser);
        saveUserToken(hubUser, jwtToken);
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String hubUserUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        hubUserUsername = jwtService.extractUsername(refreshToken);
        if (hubUserUsername != null) {
            HubUser hubUser = this.repository.findByUsername(hubUserUsername)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, hubUser)) {
                String accessToken = jwtService.generateToken(hubUser);
                revokeAllUserTokens(hubUser);
                saveUserToken(hubUser, accessToken);
                AuthResponseDTO authResponse = AuthResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}