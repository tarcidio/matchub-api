package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.enums.Known;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTODetails {
    private ChampionDTOLinks spotlight;
    private ChampionDTOLinks opponent;
    private List<CommentDTOLinks> comments = new ArrayList<>();
}
