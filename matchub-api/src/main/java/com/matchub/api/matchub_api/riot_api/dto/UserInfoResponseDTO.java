package com.matchub.api.matchub_api.riot_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponseDTO {
    private String sub;// Puuid
    private String cpid;// Region
}
