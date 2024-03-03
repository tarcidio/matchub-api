package com.matchhub.matchhub.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.matchhub.matchhub.domain.enums.Known;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
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
    private SortedSet<Comment> comments = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) && Objects.equals(spotlight, post.spotlight) && Objects.equals(opponent, post.opponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spotlight, opponent);
    }
}
