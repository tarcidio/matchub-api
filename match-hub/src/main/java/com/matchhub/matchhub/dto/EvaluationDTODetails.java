package com.matchhub.matchhub.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTODetails extends EvaluationDTOBaseId{
    private HubUserDTOLinks hubUser;
    private CommentDTOLinks comment;
    private LocalDateTime creation;
    private LocalDateTime update;
}
