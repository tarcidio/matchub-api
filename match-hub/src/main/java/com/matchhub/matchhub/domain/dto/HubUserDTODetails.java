package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.enums.Hability;
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
// Example of used: HubUser Get
public class HubUserDTODetails extends HubUserDTOBase {
    private Hability abilityLevel;
    private ChampionDTOLinks mastery;
    private LocalDateTime creation;
    private LocalDateTime update;
    private List<CommentDTOLinks> comments = new ArrayList<>();
    private List<EvaluationDTOLinks> evaluations = new ArrayList<>();
}


//base: dto que tem as informacoes basicas q todos que herdarem vao ter (retirada de referencias e as vezes o time stamp)
//details: que possui todos os detalhes que qq um pode tem inclusive no get da prorpia entidade
//links: base com links das referencias (se n for referncia n eh assiciono
//custom: destinado a visualizacao para outra atividade

//base: info basicas
