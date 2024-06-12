package com.matchub.api.matchub_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDTOLinks extends ScreenDTOBaseId {
    private Long spotlightId;
    private Long opponentId;
}
