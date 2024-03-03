package com.matchhub.matchhub.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.enums.Hability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HubUserBaseDTO {
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
