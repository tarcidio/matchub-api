package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.dto.EvaluationDTOBase;
import com.matchhub.matchhub.dto.EvaluationDTOLinks;
import com.matchhub.matchhub.repository.EvaluationRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService{
    private final EvaluationRepository evaluationRepository;
    private final CommentService commentService;
    private final HubUserService hubUserService;
    private final ModelMapper modelMapper;

    public Evaluation findDomainById(Long id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        return evaluation.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Evaluation.class.getName()));
    }

    public EvaluationDTOLinks save(Long commentId,
                                   EvaluationDTOBase evaluation,
                                   Principal connectedHubUser) {
        //Get Comment
        Comment comment = commentService.findDomainById(commentId);
        // Get User Logged
        HubUser hubUser = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Transfer information
        Evaluation saveEvaluation = modelMapper.map(evaluation, Evaluation.class);
        //Set Id, Comment and User
        saveEvaluation.setId(null);
        saveEvaluation.setComment(comment);
        saveEvaluation.setHubUser(hubUser);
        return modelMapper.map(evaluationRepository.save(saveEvaluation), EvaluationDTOLinks.class);
    }

    public EvaluationDTOLinks update(Long commentId,
                                     Long evaluationId,
                                     EvaluationDTOBase evaluation,
                                     Principal connectedHubUser) {
        // Get evaluation
        Evaluation updatedEvaluation = this.findDomainById(evaluationId);
        // Get User Logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();

        // Check if comment exists in screen
        if(!updatedEvaluation.getComment().getId().equals(commentId)){
            throw new IllegalArgumentException("Update is incompatible with the indicated screen.");
        }
        //Check if comment belong to authenticated hubUser id
        if (!updatedEvaluation.getHubUser().getId().equals(logged.getId())){
            throw new IllegalArgumentException("Update isn't allow.");
        }

        // Convert
        modelMapper.map(evaluation, updatedEvaluation);
        // Return response in correct format
        return modelMapper.map(evaluationRepository.save(updatedEvaluation), EvaluationDTOLinks.class);
    }

    public void delete(Long evaluationId, Principal connectedHubUser) {
        // Get the comment
        Evaluation deletedEvaluation = this.findDomainById(evaluationId);
        // Get user connected
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Check if comment belong to authenticated hubUser id
        if (!deletedEvaluation.getHubUser().getId().equals(logged.getId())){
            throw new IllegalArgumentException("Update isn't allow.");
        }
        // Delete evaluation
        evaluationRepository.deleteById(evaluationId);
    }
}
