package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationBaseDTO {
    private Long id;
    private HubUser hubUser;
    private Comment comment;
    private EvaluationLevel level;
    private LocalDateTime creation;
    private LocalDateTime update;
}
