package com.matchhub.matchhub.dto;

import com.matchhub.matchhub.domain.enums.Hability;
import com.matchhub.matchhub.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Full Hub User Schema")
public class HubUserDTODetails extends HubUserDTOBaseId {
//    @Schema(description = "Login")
//    private String username;
//    @Schema(description = "Password")
//    private String password;
    @Schema(description = "References about permission to comment")
    private boolean blocked = false;
    @Schema(description = "HubUser Role (Admin, Moderator or User)")
    private Role role = Role.HUBUSER;
    @Schema(description = "Level of skill that the user has with the most mastered champion")
    private Hability abilityLevel;
    @Schema(description = "The most mastered champion")
    private ChampionDTOLinks mastery;
    @Schema(description = "Account create datetime")
    private LocalDateTime creation;
    @Schema(description = "Account update datetime")
    private LocalDateTime update;
    @Schema(description = "Comments that the user has already made")
    private List<CommentDTOLinks> comments = new ArrayList<>();
    @Schema(description = "Evaluations that the user has already made")
    private List<EvaluationDTOLinks> evaluations = new ArrayList<>();
}


//base: dto que tem as informacoes basicas q todos que herdarem vao ter (retirada de referencias e as vezes o time stamp)
//details: que possui todos os detalhes que qq um pode tem inclusive no get da prorpia entidade
//links: base com links das referencias (se n for referncia n eh assiciono
//custom: destinado a visualizacao para outra atividade

//base: info basicas
