package com.matchub.api.matchub_api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"hubUser_id", "screen_id", "creationDate", "creationTime"})
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hubUser_id")
    private HubUser hubUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private LocalDate creationDate;

    @Temporal(TemporalType.TIME)
    @CreationTimestamp
    private LocalTime creationTime;

    @Temporal(TemporalType.DATE)
    @UpdateTimestamp
    private LocalDate updateDate;

    @Temporal(TemporalType.TIME)
    @UpdateTimestamp
    private LocalTime updateTime;

    @Column(nullable = false)
    private String text = "";

    @Column(nullable = false)
    private Integer numGoodEvaluation = 0;

    @Column(nullable = false)
    private Integer numBadEvaluation = 0;

    @Column(nullable = false)
    private Integer maxNumGoodEvaluation = 0;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Evaluation> evaluations = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(hubUser, comment.hubUser) && Objects.equals(screen, comment.screen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hubUser, screen);
    }
}
