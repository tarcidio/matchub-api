package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTOLinks extends EvaluationDTOBase{
    private Long hubUserId;
    private Long commentId;
}
