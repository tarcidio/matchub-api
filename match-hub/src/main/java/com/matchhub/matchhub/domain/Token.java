package com.matchhub.matchhub.domain;

import com.matchhub.matchhub.domain.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token"})
})
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked = false;

    public boolean expired = false;

    @ManyToOne
    @JoinColumn(name = "hubUser_id")
    public HubUser hubUser;
}
