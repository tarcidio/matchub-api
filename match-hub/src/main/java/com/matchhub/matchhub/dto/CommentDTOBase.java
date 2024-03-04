package com.matchhub.matchhub.dto;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
