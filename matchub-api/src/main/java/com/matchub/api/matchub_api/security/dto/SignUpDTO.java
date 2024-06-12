package com.matchub.api.matchub_api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private String username;
    private String password;

    private String nickname;
    private String firstname;
    private String lastname;
    private String email;
}
