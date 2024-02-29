package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Post findBySpotlight_IdAndOpponent_Id(Long spotlightId, Long opponentId);
}
