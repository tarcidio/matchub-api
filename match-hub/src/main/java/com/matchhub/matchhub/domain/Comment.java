package com.matchhub.matchhub.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
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
    private Set<Evaluation> evaluations;
}
