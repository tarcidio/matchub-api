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
public class ScreenDTOLinks extends ScreenDTOBaseId {
    private Long spotlightId;
    private Long opponentId;
}