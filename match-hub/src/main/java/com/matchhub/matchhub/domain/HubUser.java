package com.matchhub.matchhub.domain;

import com.fasterxml.jackson.annotation.*;
import com.matchhub.matchhub.domain.enums.Hability;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hubuser", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"login"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class HubUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private String firstname;

    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String login;

    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private Boolean moderator;

    @Enumerated(value = EnumType.STRING)
    @Schema(description = "Skill level of the most mastered champion", example = "Hability.NORMAL")
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
    private List<Comment> comments = new ArrayList<>();

    /* Goal: visualize all e evaluation*/
    @OneToMany(mappedBy = "hubUser", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HubUser hubUser = (HubUser) o;
        return Objects.equals(id, hubUser.id) && Objects.equals(email, hubUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
