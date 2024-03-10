package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.dto.EvaluationDTOBase;
import com.matchhub.matchhub.dto.EvaluationDTOLinks;
import com.matchhub.matchhub.service.EvaluationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@Tag(name = "Evaluation", description = "")
@RestController
@RequestMapping(value = "/comments/{commentId}/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    /* Disabled: Screen already have evaluations collections */
//    @GetMapping(value = "/{evaluationId}")
//    public ResponseEntity<Evaluation> findById(@PathVariable Long evaluationId){
//        Evaluation evaluation = evaluationService.findById(evaluationId);
//        return ResponseEntity.ok().body(evaluation);
//    }

    @PostMapping
    public ResponseEntity<EvaluationDTOLinks> create(@PathVariable Long commentId,
                                                     @RequestBody EvaluationDTOBase evaluation,
                                                     Principal connectedHubUser){
        EvaluationDTOLinks savedEvaluation = evaluationService.save(commentId, evaluation, connectedHubUser);
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
                                                     @RequestBody EvaluationDTOBase evaluation,
                                                     Principal connectedHubUser){
        EvaluationDTOLinks updatedEvaluation = evaluationService.update(commentId, evaluationId, evaluation, connectedHubUser);
        return ResponseEntity.ok().body(updatedEvaluation);
    }

    @DeleteMapping(value = "/{evaluationId}")
    public ResponseEntity<Void> delete(@PathVariable Long evaluationId,
                                       Principal connectedHubUser){
        evaluationService.delete(evaluationId, connectedHubUser);
        return ResponseEntity.noContent().build();
    }
}
