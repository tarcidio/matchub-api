package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.enums.Known;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTOBase {
    private Long id;
//    private Champion spotlight;
//    private Champion opponent;
    private Known known;
//    private SortedSet<Comment> comments = new TreeSet<>();
}
