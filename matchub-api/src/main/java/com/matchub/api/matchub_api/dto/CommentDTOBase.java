package com.matchub.api.matchub_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOBase{
//    private Long id;
//    private HubUser hubUser;
//    private Screen screen;
//    private LocalDate creationDate;
//    private LocalTime creationTime;
//    private LocalDate updateDate;
//    private LocalTime updateTime;
    @Schema(description = "", example = "null")
    private String text;
//    private Set<Evaluation> evaluations = new HashSet<>();
}
