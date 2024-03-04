package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.dto.EvaluationDTOBase;
import com.matchhub.matchhub.dto.EvaluationDTOLinks;
import com.matchhub.matchhub.service.CommentService;
import com.matchhub.matchhub.service.EvaluationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Evaluation", description = "")
@RestController
@RequestMapping(value = "/comments/{commentId}/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationController(EvaluationService evaluationService){
        this.evaluationService = evaluationService;
    }

    /* Disabled: Screen already have evaluations collections */
//    @GetMapping(value = "/{evaluationId}")
//    public ResponseEntity<Evaluation> findById(@PathVariable Long evaluationId){
//        Evaluation evaluation = evaluationService.findById(evaluationId);
//        return ResponseEntity.ok().body(evaluation);
//    }

    @PostMapping
    public ResponseEntity<EvaluationDTOLinks> create(@PathVariable Long commentId,
                                                     @RequestBody EvaluationDTOBase evaluation){
        EvaluationDTOLinks savedEvaluation = evaluationService.save(commentId, evaluation);
        URI uri = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(savedEvaluation.getId()).
                toUri();
        return ResponseEntity.created(uri).body(savedEvaluation);
    }

    @PutMapping(value = "/{evaluationId}")
    public ResponseEntity<EvaluationDTOLinks> update(@PathVariable Long commentId,
                                             @PathVariable Long evaluationId,
                                             @RequestBody EvaluationDTOBase evaluation){
        EvaluationDTOLinks updatedEvaluation = evaluationService.update(commentId, evaluationId, evaluation);
        return ResponseEntity.ok().body(updatedEvaluation);
    }

    @DeleteMapping(value = "/{evaluationId}")
    public ResponseEntity<Void> delete(@PathVariable Long evaluationId){
        evaluationService.delete(evaluationId);
        return ResponseEntity.noContent().build();
    }
}
