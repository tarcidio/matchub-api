package com.matchhub.matchhub.riot_api.dto;

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
