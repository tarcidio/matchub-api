package com.matchhub.matchhub.security.dto;

import com.matchhub.matchhub.domain.enums.Region;
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
public class SingUpDTO {
    private String username;
    private String password;

    private String nickname;
    private String firstname;
    private String lastname;
    private String email;
    private Region region;
}
