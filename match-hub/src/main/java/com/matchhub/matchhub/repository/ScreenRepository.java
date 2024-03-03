package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.domain.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

    Screen findBySpotlight_IdAndOpponent_Id(Long spotlightId, Long opponentId);
}
