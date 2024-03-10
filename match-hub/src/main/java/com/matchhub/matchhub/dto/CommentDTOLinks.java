package com.matchhub.matchhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOLinks extends CommentDTOBaseId {
    private Long hubUserId;
    private Long screenId;
    private LocalDate creationDate;
    private LocalTime creationTime;
    private LocalDate updateDate;
    private LocalTime updateTime;
}
