package com.matchhub.matchhub.security.token.service;

import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.repository.TokenRepository;
import com.matchhub.matchhub.security.token.domain.Token;
import com.matchhub.matchhub.security.token.domain.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

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
}
