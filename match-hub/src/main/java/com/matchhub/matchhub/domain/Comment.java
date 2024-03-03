package com.matchhub.matchhub.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Comment implements Comparable<Comment>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private HubUser hubUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private LocalDate creationDate;

    @Temporal(TemporalType.TIME)
    @CreationTimestamp
    private LocalTime creationTime;

    private String text;

    @OneToMany(mappedBy = "comment")
    private Set<Evaluation> evaluations = new HashSet<>();

    @Override
    public int compareTo(Comment other) {
        long myGoodEvaluations = this.evaluations.stream()
                .filter(evaluation -> evaluation.getLevel().equals(EvaluationLevel.GOOD))
                .count();
        long otherGoodEvaluations = other.getEvaluations().stream()
                .filter(evaluation -> evaluation.getLevel().equals(EvaluationLevel.GOOD))
                .count();
        return Long.compare(myGoodEvaluations, otherGoodEvaluations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(hubUser, comment.hubUser) && Objects.equals(post, comment.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hubUser, post);
    }
}
