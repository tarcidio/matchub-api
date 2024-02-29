package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.domain.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRepository extends JpaRepository<Champion,Long> {
}
