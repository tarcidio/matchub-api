package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
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
public class CommentDetailsDTO extends CommentBaseDTO{
    private HubUser hubUser;
    private Long postId;
    private Set<Evaluation> evaluations = new HashSet<>();
}
