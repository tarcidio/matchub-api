package com.matchhub.matchhub.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubUserDTOBase {
//    private Long id;
    @Schema(description = "", example = "null")
    private String nickname;
    @Schema(description = "", example = "null")
    private String firstname;
    @Schema(description = "", example = "null")
    private String lastname;
    @Schema(description = "", example = "null")
    private String email;
    @Schema(description = "", example = "null")
    private String username;
    @Schema(description = "", example = "RETIRE ESSE CAMPO DO DTO E TIRE O COMENTARIO DO NOT NULL DO DOMAIN")
    private String password;
    @Schema(description = "Indicates if user is moderator or not", example = "false")
    private Boolean moderator;
//    private Hability abilityLevel;
//    private Champion mastery;
//    private LocalDateTime creation;
//    private LocalDateTime update;
//    private SortedSet<Comment> comments = new TreeSet<>();
//    private Set<Evaluation> evaluations = new HashSet<>();
}
