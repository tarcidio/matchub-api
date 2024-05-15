package com.matchhub.matchhub.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matchhub.matchhub.dto.HubUserDTODetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    @JsonProperty("accessToken")
    private String accessToken;
}
