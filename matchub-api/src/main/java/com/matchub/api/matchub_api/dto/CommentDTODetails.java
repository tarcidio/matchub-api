package com.matchub.api.matchub_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTODetails extends CommentDTOBaseId{
    private Integer numGoodEvaluation;
    private Integer numBadEvaluation;
    private LocalDate creationDate;
    private LocalTime creationTime;
    private LocalDate updateDate;
    private LocalTime updateTime;
    private HubUserDTOLinks hubUser;
    private ScreenDTOLinks screen;
    private List<EvaluationDTOLinks> evaluations = new ArrayList<>();
}
