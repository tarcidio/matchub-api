package com.matchhub.matchhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTODetails extends ScreenDTOBaseId {
    //Specially in this case, it's necessary bring details, because Screen is an entity very important
    private ChampionDTODetails spotlight;
    private ChampionDTODetails opponent;
    private List<CommentDTODetails> comments = new ArrayList<>();
}
