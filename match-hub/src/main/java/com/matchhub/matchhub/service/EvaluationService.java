package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Evaluation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService{
    public Evaluation findByPost_IdAndComment_IdAndEvaluation_Id(Long postId, Long commentId, Long evaluationId) {
        return null;
    }

    public List<Evaluation> findAllByPost_IdAndComment_Id(Long postId, Long commentId) {
        return null;
    }

    public Evaluation save(Long postId, Long commentId, Evaluation evaluation) {
        return null;
    }

    public Evaluation update(Long postId, Long commentId, Long evaluationId, Evaluation evaluation) {
        return null;
    }

    public void delete(Long postId, Long commentId, Long evaluationId) {

    }
}
