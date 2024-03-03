package com.matchhub.matchhub.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hubUser_id")
    private HubUser hubUser;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Enumerated(EnumType.ORDINAL)
    private EvaluationLevel level;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime creation;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private LocalDateTime update;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluation that = (Evaluation) o;
        return Objects.equals(id, that.id) && Objects.equals(hubUser, that.hubUser) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hubUser, comment);
    }
}
