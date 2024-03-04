package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
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
    public EvaluationService(EvaluationRepository evaluationRepository, CommentService commentService, HubUserService hubUserService, ModelMapper modelMapper) {
        this.evaluationRepository = evaluationRepository;
        this.commentService = commentService;
        this.hubUserService = hubUserService;
        this.modelMapper = modelMapper;
    }

    public Evaluation findById(Long id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        return evaluation.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Evaluation.class.getName()));
    }

    public Evaluation save(Long commentId, Evaluation evaluation) {
        //Get Comment
        Comment comment = commentService.findDomainById(commentId);
        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId
        //3. Use HubUserService to get object
        // Provisional:
        HubUser hubUser = new HubUser();
        hubUser.setId(evaluation.getHubUser().getId());

        //Validate new evaluation with comment id
        if (evaluation.getComment() != null && !evaluation.getComment().getId().equals(comment.getId())) {
            //Customize exception
            throw new IllegalArgumentException("Evaluation is incompatible with the indicated comment.");
        }
        //Validate new evaluation with authenticated hubUser id
//        if (evaluation.getHubUser() != null && !evaluation.getHubUser().getId().equals(hubUser.getId())) {
//            //Customize exception
//            throw new IllegalArgumentException("Comment is incompatible with the indicated screen.");
//        }

        //Set Id, Comment and User
        evaluation.setId(null);
        evaluation.setComment(comment);
        evaluation.setHubUser(hubUser);

        return evaluationRepository.save(evaluation);
    }


    public Evaluation update(Long commentId, Long evaluationId, Evaluation evaluation) {
        ///Get evaluation
        Evaluation updatedEvaluation = this.findById(evaluationId);
        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId

        //Check if evaluation exists in comment
        if(!updatedEvaluation.getComment().getId().equals(commentId)){
            throw new IllegalArgumentException("Update is incompatible with the indicated comment.");
        }
        //Check if evaluation belong to authenticated hubUser id
//      if (updatedEvaluation.getHubUser().getId().equals(hubUser.getId())){
//          throw new IllegalArgumentException("Update isn't allow.");
//      }

        //Validate new body evaluation with comment id
        if (evaluation.getComment() != null && !evaluation.getComment().getId().equals(commentId)) {
            throw new IllegalArgumentException("Evaluation is incompatible with the indicated comment.");
        }

        //Validate new body evaluation with authenticate hubUser id
//        if (evaluation.getHubUser() != null && !evaluation.getHubUser().getId().equals(hubUser.getId())){
//            throw new IllegalArgumentException("Update isn't allow.");
//        }

        //No make sense, because comment don't have id (it's null)
//        if (!evaluation.getId().equals(evaluationId)) {
//            throw new IllegalArgumentException("");
//        }

        modelMapper.map(evaluation, updatedEvaluation);
        return evaluationRepository.save(updatedEvaluation);
    }

    public void delete(Long evaluationId) {
        /*Need authentication*/
        //Validate Id, Screen and User
        evaluationRepository.deleteById(evaluationId);
    }
}
