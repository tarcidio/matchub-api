package com.matchub.api.matchub_api.repository;

import com.matchub.api.matchub_api.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
