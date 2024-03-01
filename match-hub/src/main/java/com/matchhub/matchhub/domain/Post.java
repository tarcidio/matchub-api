package com.matchhub.matchhub.domain;

import com.matchhub.matchhub.domain.enums.Known;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "spotlight_id")
    private Champion spotlight;

    @ManyToOne(optional = false)
    @JoinColumn(name = "opponent_id")
    private Champion opponent;

    @Enumerated(value = EnumType.STRING)
    private Known known;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments;
}
