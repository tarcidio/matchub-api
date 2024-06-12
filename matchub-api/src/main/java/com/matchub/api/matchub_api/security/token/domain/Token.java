package com.matchub.api.matchub_api.security.token.domain;

import com.matchub.api.matchub_api.domain.HubUser;
import com.matchub.api.matchub_api.security.token.domain.enums.TokenType;
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
    private Integer id;

    @Column(nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean revoked = false;

    private boolean expired = false;

    @ManyToOne
    @JoinColumn(name = "hubUser_id")
    private HubUser hubUser;
}
