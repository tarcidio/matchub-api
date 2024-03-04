package com.matchhub.matchhub.dto;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTOBase {
//    private Long id;
//    private HubUser hubUser;
//    private Comment comment;
    //@Schema(description = "", example = "GOOD")
    @Enumerated(EnumType.ORDINAL)
    private EvaluationLevel level;
//    private LocalDateTime creation;
//    private LocalDateTime update;
}
