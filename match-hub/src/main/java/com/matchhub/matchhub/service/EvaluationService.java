package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.dto.CommentDTOLinks;
import com.matchhub.matchhub.dto.EvaluationDTOBase;
import com.matchhub.matchhub.dto.EvaluationDTOLinks;
import com.matchhub.matchhub.repository.EvaluationRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationService{

    private final EvaluationRepository evaluationRepository;

    private final CommentService commentService;
    private final HubUserService hubUserService;

    private final ModelMapper modelMapper;


    @Autowired
    public EvaluationService(EvaluationRepository evaluationRepository,
                             CommentService commentService,
                             HubUserService hubUserService,
                             ModelMapper modelMapper) {
        this.evaluationRepository = evaluationRepository;
        this.commentService = commentService;
        this.hubUserService = hubUserService;
        this.modelMapper = modelMapper;
    }

    public Evaluation findByDomainId(Long id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        return evaluation.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Evaluation.class.getName()));
    }

    public EvaluationDTOLinks save(Long commentId, EvaluationDTOBase evaluation) {
        //Get Comment
        Comment comment = commentService.findDomainById(commentId);

        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId
        //3. Use HubUserService to get object
        // Provisional:
        HubUser hubUser = new HubUser();
        hubUser.setId(2L);

        System.out.println("Chegou auqi");

        // Create repository instance
        Evaluation saveEvaluation = new Evaluation();
        // Convert
        modelMapper.map(evaluation, saveEvaluation);
        //Set Id, Screen and User
        saveEvaluation.setId(null);
        saveEvaluation.setComment(comment);
        saveEvaluation.setHubUser(hubUser);
        return modelMapper.map(evaluationRepository.save(saveEvaluation), EvaluationDTOLinks.class);
    }


    public EvaluationDTOLinks update(Long commentId, Long evaluationId, EvaluationDTOBase evaluation) {
        ///Get evaluation
        Evaluation updatedEvaluation = this.findByDomainId(evaluationId);
        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId

        //Check if comment exists in screen
        if(!updatedEvaluation.getComment().getId().equals(commentId)){
            throw new IllegalArgumentException("Update is incompatible with the indicated comment.");
        }
        //Check if comment belong to authenticated hubUser id
//      if (updatedEvaluation.getHubUser().getId().equals(hubUser.getId())){
//          throw new IllegalArgumentException("Update isn't allow.");
//      }

        // Convert
        modelMapper.map(evaluation, updatedEvaluation);
        // Return response in correct format
        return modelMapper.map(evaluationRepository.save(updatedEvaluation), EvaluationDTOLinks.class);
    }

    public void delete(Long evaluationId) {
        /*Need authentication*/
        //Validate Id, Screen and User
        evaluationRepository.deleteById(evaluationId);
    }
}
