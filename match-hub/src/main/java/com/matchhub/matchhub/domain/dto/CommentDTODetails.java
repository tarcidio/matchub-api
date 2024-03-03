package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTODetails extends CommentDTOBase {
    private HubUser hubUser;
    private Screen screenId;
    private Set<Evaluation> evaluations = new HashSet<>();
}
