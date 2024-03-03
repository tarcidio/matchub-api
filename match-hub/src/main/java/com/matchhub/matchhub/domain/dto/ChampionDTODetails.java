package com.matchhub.matchhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChampionDTODetails extends ChampionDTOBase {
    private Byte[] img;
}