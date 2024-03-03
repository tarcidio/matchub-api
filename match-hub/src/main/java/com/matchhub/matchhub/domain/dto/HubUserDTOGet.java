package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Champion;
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
public class HubUserDTOGet extends HubUserBaseDTO{
    private Hability abilityLevel;
    private Champion mastery;
    private LocalDateTime creation;
    private LocalDateTime update;
    private SortedSet<Comment> comments = new TreeSet<>();
    private Set<Evaluation> evaluations = new HashSet<>();
}
