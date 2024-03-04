package com.matchhub.matchhub.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOLinks extends CommentDTOBaseId {
    private Long hubUserId;
    private Long screenId;
}
