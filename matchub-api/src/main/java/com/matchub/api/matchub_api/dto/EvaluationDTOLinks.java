package com.matchub.api.matchub_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTOLinks extends EvaluationDTOBaseId{
    private Long hubUserId;
    private Long commentId;
    private LocalDateTime creation;
    private LocalDateTime update;
}
