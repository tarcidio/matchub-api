package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.security.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
            // WHERE u.id = :id AND (t.expired = false OR t.revoked = false)s
    List<Token> findAllValidTokenByUser(Long id);
    Optional<Token> findByToken(String token);
}
