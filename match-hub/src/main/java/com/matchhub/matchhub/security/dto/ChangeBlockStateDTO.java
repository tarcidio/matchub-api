package com.matchhub.matchhub.security.dto;

import com.matchhub.matchhub.domain.enums.Role;
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
