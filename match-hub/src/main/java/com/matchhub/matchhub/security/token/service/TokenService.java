package com.matchhub.matchhub.security.token.service;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.security.jwt.JwtService;
import com.matchhub.matchhub.security.token.repository.TokenRepository;
import com.matchhub.matchhub.security.token.domain.Token;
import com.matchhub.matchhub.security.token.domain.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public void saveUserToken(HubUser hubUser, String jwtToken) {
        Token token = Token.builder()
                .hubUser(hubUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(HubUser hubUser) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(hubUser.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    @Scheduled(cron = "0 0 3 * * ?") // Run at 3 A.M.
    public void cleanExpiredTokens() {
        List<Token> expiredTokens = findExpiredTokens();
        tokenRepository.deleteAll(expiredTokens);
    }

    private List<Token> findExpiredTokens() {
        List<Token> allTokens = tokenRepository.findAll();
        return allTokens.stream()
                .filter(token -> jwtService.isTokenExpired(token.getToken()) || token.isExpired() || token.isRevoked())
                .collect(Collectors.toList());
    }

    public String generateRefreshToken(HubUser hubUser){
        return jwtService.generateRefreshToken(hubUser);
    }

    public String generateToken(HubUser hubUser){
        return jwtService.generateToken(hubUser);
    }

    public String extractUsername(String token){
        return jwtService.extractUsername(token);
    }

    public boolean isTokenValid(String token, HubUser hubUser){
        return jwtService.isTokenValid(token, hubUser);
    }

    public Token findByToken(String jwt){
        return tokenRepository.findByToken(jwt)
                .orElse(null);
    }

    public Token saveToken(Token token){
        return tokenRepository.save(token);
    }

}
