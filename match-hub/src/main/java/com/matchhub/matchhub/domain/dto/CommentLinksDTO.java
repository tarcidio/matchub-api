package com.matchhub.matchhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentLinksDTO extends CommentBaseDTO{
    /*This class have just id (links) to other classes*/
    private Long hubUserId;
    private Long postId;
}
