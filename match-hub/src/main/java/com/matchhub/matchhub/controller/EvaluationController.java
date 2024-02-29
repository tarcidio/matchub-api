package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.service.CommentService;
import com.matchhub.matchhub.service.EvaluationService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/posts/{postId}/comments/{commentId}/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService){
        this.evaluationService = evaluationService;
    }

    @GetMapping(value = "/{evaluationId}")
    public ResponseEntity<Evaluation> findById(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @PathVariable Long evaluationId){
        Evaluation evaluation = evaluationService.findByPost_IdAndComment_IdAndEvaluation_Id(postId, commentId, evaluationId);
        return ResponseEntity.ok().body(evaluation);
    }

    @GetMapping
    public ResponseEntity<List<Evaluation>> findAll(@PathVariable Long postId,
                                                    @PathVariable Long commentId){
        List<Evaluation> evaluations = evaluationService.findAllByPost_IdAndComment_Id(postId, commentId);
        return ResponseEntity.ok().body(evaluations);
    }

    @PostMapping
    public ResponseEntity<Evaluation> create(@PathVariable Long postId,
                                             @PathVariable Long commentId,
                                             @RequestBody Evaluation evaluation){
        Evaluation savedEvaluation = evaluationService.save(postId, commentId, evaluation);
        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand().
                toUri();
        return ResponseEntity.created(uri).body(savedEvaluation);
    }

    @PutMapping(value = "/{evaluationId}")
    public ResponseEntity<Evaluation> update(@PathVariable Long postId,
                                             @PathVariable Long commentId,
                                             @PathVariable Long evaluationId,
                                             @PathVariable Evaluation evaluation){
        Evaluation updatedEvaluation = evaluationService.update(postId, commentId, evaluationId, evaluation);
        return ResponseEntity.ok().body(updatedEvaluation);
    }

    @DeleteMapping(value = "/{evaluationId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId,
                                       @PathVariable Long commentId,
                                       @PathVariable Long evaluationId){
        evaluationService.delete(postId, commentId, evaluationId);
        return ResponseEntity.noContent().build();
    }
}
