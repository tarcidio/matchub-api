package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.domain.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Optional<Screen> findBySpotlight_IdAndOpponent_Id(Long spotlightId, Long opponentId);
}
