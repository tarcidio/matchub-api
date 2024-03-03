package com.matchhub.matchhub.domain;

import com.matchhub.matchhub.domain.enums.Known;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Screen {
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

    @OneToMany(mappedBy = "screen", cascade = CascadeType.ALL)
    private SortedSet<Comment> comments = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screen screen = (Screen) o;
        return Objects.equals(id, screen.id) && Objects.equals(spotlight, screen.spotlight) && Objects.equals(opponent, screen.opponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spotlight, opponent);
    }
}
