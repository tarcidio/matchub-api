package com.matchub.api.matchub_api.repository;

import com.matchub.api.matchub_api.domain.HubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HubUserRepository extends JpaRepository<HubUser, Long> {
    Optional<HubUser> findByUsername(String hubUserUsername);

    Optional<HubUser> findByEmail(String hubUserEmail);
}
