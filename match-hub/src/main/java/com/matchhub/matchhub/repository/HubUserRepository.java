package com.matchhub.matchhub.repository;

import com.matchhub.matchhub.domain.HubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubUserRepository extends JpaRepository<HubUser, Long> {
}
