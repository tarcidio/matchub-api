package com.matchhub.matchhub.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubUserDTOBase {
    private Long id;
    private String nickname;
    private String firstname;
    private String lastname;
    private String email;
    private String login;
    private String password;
    private Boolean moderator;
//    private Hability abilityLevel;
//    private Champion mastery;
//    private LocalDateTime creation;
//    private LocalDateTime update;
//    private SortedSet<Comment> comments = new TreeSet<>();
//    private Set<Evaluation> evaluations = new HashSet<>();
}
