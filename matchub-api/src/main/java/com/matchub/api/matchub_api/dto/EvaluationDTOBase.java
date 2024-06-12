package com.matchub.api.matchub_api.dto;

import com.matchub.api.matchub_api.domain.enums.EvaluationLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
