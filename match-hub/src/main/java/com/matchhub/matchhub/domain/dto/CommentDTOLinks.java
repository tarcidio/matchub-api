package com.matchhub.matchhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTOLinks extends CommentDTOBase {
    private Long hubUserId;
    private Long screenId;
}