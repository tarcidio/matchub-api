package com.matchhub.matchhub.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Token;
import com.matchhub.matchhub.domain.enums.TokenType;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.repository.TokenRepository;
import com.matchhub.matchhub.security.dto.AuthResponseDTO;
import com.matchhub.matchhub.security.dto.LoginDTO;
import com.matchhub.matchhub.security.dto.SignUpDTO;
import com.matchhub.matchhub.security.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

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

    private Cookie createCookie(String refreshToken){
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);// Cookie can't be managed in client side
        refreshCookie.setSecure(true); // Cookie can be used only secure https connections
        refreshCookie.setPath("/");// Cookie will send to all domain request
        refreshCookie.setMaxAge((int) refreshExpiration);
        return refreshCookie;
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

    private AuthResponseDTO manageTokens(HubUser hubUser, HttpServletResponse response){
        // Generate tokens
        String jwtToken = jwtService.generateToken(hubUser);
        String refreshToken = jwtService.generateRefreshToken(hubUser);
        // Delete all tokens from hubUser
        revokeAllUserTokens(hubUser);
        // Salve tokens
        saveUserToken(hubUser, jwtToken);

        // Put refreshToken in Cookies
        response.addCookie(createCookie(refreshToken));

        // Return response
        return AuthResponseDTO.builder()
                .accessToken(jwtToken)
                .nickname(hubUser.getNickname())
                .build();
    }

    //SignUp
    public AuthResponseDTO register(SignUpDTO request, HttpServletResponse response) {
        // Transfer information
        HubUser newHubUser = modelMapper.map(request, HubUser.class);
        // Set id null (good practice)
        newHubUser.setId(null);
        // Encode password
        newHubUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save user
        HubUser savedHubUser = repository.save(newHubUser);

        return manageTokens(savedHubUser, response);
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
        return manageTokens(hubUser, response);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println("Nome do cookie eh: " + cookie.getName());
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // Retorna null se o cookie não for encontrado
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String hubUserUsername;
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return;
//        }
        refreshToken = getRefreshTokenFromCookie(request);
        System.out.println("Meu refresh token eh: " + refreshToken);
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
                        .nickname(hubUser.getNickname())
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}