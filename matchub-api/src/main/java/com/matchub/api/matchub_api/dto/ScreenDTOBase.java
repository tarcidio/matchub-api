package com.matchub.api.matchub_api.dto;

import com.matchub.api.matchub_api.domain.enums.Known;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTOBase {
//    private Long id;
//    private Champion spotlight;
//    private Champion opponent
    private Known known;
//    private SortedSet<Comment> comments = new TreeSet<>();
}
