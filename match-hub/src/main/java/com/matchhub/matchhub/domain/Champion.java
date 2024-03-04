package com.matchhub.matchhub.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "champion", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Champion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Byte[] img;
    //Note: don't list champion user either screen. Then, it don't bidirectional

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Champion champion = (Champion) o;
        return Objects.equals(id, champion.id) && Objects.equals(name, champion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
