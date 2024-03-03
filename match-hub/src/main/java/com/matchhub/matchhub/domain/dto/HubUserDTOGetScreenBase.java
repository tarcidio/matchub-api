package com.matchhub.matchhub.domain.dto;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.enums.Hability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubUserDTOGetScreenBase extends HubUserDTOBase {
    private Hability abilityLevel;
    private Champion mastery;
    private LocalDateTime creation;
}
