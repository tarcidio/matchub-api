package com.matchub.api.matchub_api.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeBlockStateDTO {
    @Schema(description = "Block state user", example = "false")
    private Boolean blocked = false;
}
