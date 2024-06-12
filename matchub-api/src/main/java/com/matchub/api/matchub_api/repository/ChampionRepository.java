package com.matchub.api.matchub_api.repository;

import com.matchub.api.matchub_api.domain.Champion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionRepository extends JpaRepository<Champion,Long> {
}
