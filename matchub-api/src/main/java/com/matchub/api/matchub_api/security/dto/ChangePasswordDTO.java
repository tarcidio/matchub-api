package com.matchub.api.matchub_api.security.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}