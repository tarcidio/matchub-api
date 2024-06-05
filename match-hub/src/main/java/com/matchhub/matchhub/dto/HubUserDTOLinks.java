package com.matchhub.matchhub.dto;

import com.matchhub.matchhub.domain.enums.Hability;
import com.matchhub.matchhub.security.token.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class HubUserDTOLinks extends HubUserDTOBaseId{
    @Schema(description = "Login")
    private String username;
//    @Schema(description = "Password")
//    private String password;
    @Schema(description = "Name in Riot Account")
    private String summonerName;
    @Schema(description = "References about permission to comment")
    private boolean blocked = false;
    @Schema(description = "HubUser Role (Admin, Moderator or User)")
    private Role role = Role.HUBUSER;
    @Schema(description = "Level of skill that the user has with the most mastered champion")
    private Hability abilityLevel;
    @Schema(description = "The most mastered champion")
    private Long masteryId;
    @Schema(description = "Account create datetime")
    private LocalDateTime creation;
    @Schema(description = "Account update datetime")
    private LocalDateTime update;
}

// It has everything it has in the details, but references to objects are made by Long id.
// It also doesn't have collections
