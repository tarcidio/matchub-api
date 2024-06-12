package com.matchub.api.matchub_api.security.token.repository;

import com.matchub.api.matchub_api.security.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            SELECT t \s
            FROM Token t \s
            JOIN HubUser u\s
            ON t.hubUser.id = u.id\s
            WHERE u.id = :id AND (t.expired = false AND t.revoked = false)\s
            """)
            // WHERE u.id = :id AND (t.expired = false OR t.revoked = false)\s
    List<Token> findAllValidTokenByUser(Long id);
    Optional<Token> findByToken(String token);

    @Transactional
    @Modifying // This query modify the database
    @Query("DELETE FROM Token t WHERE t.expired = true OR t.revoked = true")
    void deleteExpiredOrRevokedTokens();
}
