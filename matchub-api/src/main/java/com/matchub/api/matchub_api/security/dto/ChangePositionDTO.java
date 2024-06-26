package com.matchub.api.matchub_api.security.dto;

import com.matchub.api.matchub_api.security.token.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePositionDTO {
    @Schema(description = "This user's new role", example = "HUBUSER")
    private Role role = Role.HUBUSER;
}
