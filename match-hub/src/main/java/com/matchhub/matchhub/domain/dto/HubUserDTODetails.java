package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.enums.Hability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubUserDTODetails extends HubUserDTOBase {
    private Hability abilityLevel;
    private ChampionDTODetails mastery;
    private LocalDateTime creation;
    private LocalDateTime update;
    private SortedSet<Comment> comments = new TreeSet<>();
    private Set<Evaluation> evaluations = new HashSet<>();
}


//base: dto que tem as informacoes basicas q todos que herdarem vao ter (retirada de referencias e as vezes o time stamp)
//details: que possui todos os detalhes que qq um pode tem inclusive no get da prorpia entidade
//links: base com links das referencias (se n for referncia n eh assiciono
//custom: destinado a visualizacao para outra atividade
