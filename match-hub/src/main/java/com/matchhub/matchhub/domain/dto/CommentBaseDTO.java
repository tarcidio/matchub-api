package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentBaseDTO {
    private Long id;
//    private HubUser hubUser;
//    private Post post;
    private LocalDate creationDate;
    private LocalTime creationTime;
    private String text;
//    private Set<Evaluation> evaluations = new HashSet<>();
}
