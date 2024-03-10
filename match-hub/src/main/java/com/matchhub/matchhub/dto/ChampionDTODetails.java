package com.matchhub.matchhub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChampionDTODetails extends ChampionDTOBaseId {
    private Byte[] img;
}
