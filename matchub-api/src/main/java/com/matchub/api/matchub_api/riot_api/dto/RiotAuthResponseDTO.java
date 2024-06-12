package com.matchub.api.matchub_api.riot_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiotAuthResponseDTO {
    private String id_token;
    private String access_token;
    private String refresh_token;
}
