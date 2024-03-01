package com.matchhub.matchhub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matchhub.matchhub.domain.enums.Hability;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.SortedSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String firstname;

    private String lastname;

    private String email;

    private String login;

    @JsonIgnore
    private String password;

    private Boolean moderator;

    @Enumerated(value = EnumType.STRING)
    private Hability abilityLevel;

    // Champion with greatest mastery
    @ManyToOne
    @JoinColumn(name = "champion_id")
    private Champion mastery;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creation;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime update;

    /* Goal: visualize all comments of a user */
    @OneToMany(mappedBy = "hubUser", cascade = CascadeType.ALL)
    private SortedSet<Comment> comments;

    /* Goal: visualize all e evaluation*/
    @OneToMany(mappedBy = "hubUser", cascade = CascadeType.ALL)
    private SortedSet<Evaluation> evaluations;
}
