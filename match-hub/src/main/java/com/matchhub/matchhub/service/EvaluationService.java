package com.matchhub.matchhub.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchhub.matchhub.aws.SQSService;
import com.matchhub.matchhub.aws.email.EmailMessage;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.Evaluation;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import com.matchhub.matchhub.dto.EvaluationDTOBase;
import com.matchhub.matchhub.dto.EvaluationDTOLinks;
import com.matchhub.matchhub.repository.EvaluationRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final SQSService sqsService;

    @Value("${app.link.notification}")
    private String notificationLink;

    public Evaluation findDomainById(Long id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        return evaluation.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: " + id + "." +
                        "Type: " + Evaluation.class.getName()));
    }

    private void congratsNotification(String email, String maxNumGoodEvaluation, String spotlight,
                                      String opponent) throws JsonProcessingException {
        String link = notificationLink + spotlight + "/" + opponent;
        String subject = "[MatcHub] Congratulations! Your comment has reached " + maxNumGoodEvaluation + " likes on MatcHub!";
        String likeOrLikes = maxNumGoodEvaluation.equals("1") ? "like" : "likes";
        String message = "We have great news! Your comment on MatcHub has just reached "
                + maxNumGoodEvaluation + " " + likeOrLikes + "! "
                + "This shows how much your contribution is valued by our community. "
                + "We want to thank you for sharing your ideas and thoughts with us.\n\n"
                + "To view your popular comment and continue the conversation, click the link below:\n\n"
                + link
                + "\n\nKeep engaging with the community, and who knows, your next comment might be even more popular!\n\n"
                + "If you have any questions or need assistance, please do not hesitate to contact our support team.\n\n"
                + "Thank you for being a part of the MatcHub community!\n\n"
                + "Best regards,\n"
                + "MatcHub Team";
        EmailMessage emailToSQS = EmailMessage.builder()
                .email(email)
                .subject(subject)
                .text(message)
                .build();
        sqsService.sendNotificationToSQS(emailToSQS.createEmailJson());
    }

    private void increaseNumEvaluationInComment(EvaluationLevel level, Comment comment) throws JsonProcessingException {
        if (level.equals(EvaluationLevel.GOOD)) {
            comment.setNumGoodEvaluation(comment.getNumGoodEvaluation() + 1);
            if (comment.getNumGoodEvaluation() > comment.getMaxNumGoodEvaluation()) {
                comment.setMaxNumGoodEvaluation(comment.getNumGoodEvaluation());
                if (comment.getMaxNumGoodEvaluation() == 1
                        || (comment.getMaxNumGoodEvaluation() != 0 && comment.getMaxNumGoodEvaluation() % 10 == 0)) {
                    congratsNotification(
                            comment.getHubUser().getEmail(),
                            comment.getMaxNumGoodEvaluation().toString(),
                            comment.getScreen().getSpotlight().getName(),
                            comment.getScreen().getOpponent().getName()
                    );
                }

            }
        } else if (level.equals(EvaluationLevel.BAD)) {
            comment.setNumBadEvaluation(comment.getNumBadEvaluation() + 1);
        }
    }

    private void decreaseNumEvaluationInComment(EvaluationLevel level, Comment comment) {
        if (level.equals(EvaluationLevel.GOOD)) {
            comment.setNumGoodEvaluation(comment.getNumGoodEvaluation() - 1);
        } else if (level.equals(EvaluationLevel.BAD)) {
            comment.setNumBadEvaluation(comment.getNumBadEvaluation() - 1);
        }
    }

    public EvaluationDTOLinks save(Long commentId,
                                   EvaluationDTOBase evaluation,
                                   Principal connectedHubUser) throws JsonProcessingException {
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
        // Update Comment
        increaseNumEvaluationInComment(saveEvaluation.getLevel(), saveEvaluation.getComment());
        return modelMapper.map(evaluationRepository.save(saveEvaluation), EvaluationDTOLinks.class);
    }

    public EvaluationDTOLinks update(Long commentId,
                                     Long evaluationId,
                                     EvaluationDTOBase evaluation,
                                     Principal connectedHubUser) throws JsonProcessingException {
        // Get evaluation
        Evaluation updatedEvaluation = this.findDomainById(evaluationId);
        // Get User Logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();

        // Check if comment exists in screen
        if (!updatedEvaluation.getComment().getId().equals(commentId)) {
            throw new IllegalArgumentException("Update is incompatible with the indicated screen.");
        }
        //Check if comment belong to authenticated hubUser id
        if (!updatedEvaluation.getHubUser().getId().equals(logged.getId())) {
            throw new IllegalArgumentException("Update isn't allow.");
        }
        // Update Comment
        decreaseNumEvaluationInComment(updatedEvaluation.getLevel(), updatedEvaluation.getComment());
        // Convert
        modelMapper.map(evaluation, updatedEvaluation);
        // Update Comment
        increaseNumEvaluationInComment(updatedEvaluation.getLevel(), updatedEvaluation.getComment());
        // Return response in correct format
        return modelMapper.map(evaluationRepository.save(updatedEvaluation), EvaluationDTOLinks.class);
    }

    public void delete(Long evaluationId, Principal connectedHubUser) {
        // Get the comment
        Evaluation deletedEvaluation = this.findDomainById(evaluationId);
        // Get user connected
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Check if comment belong to authenticated hubUser id
        if (!deletedEvaluation.getHubUser().getId().equals(logged.getId())) {
            throw new IllegalArgumentException("Update isn't allow.");
        }
        // Update Comment
        decreaseNumEvaluationInComment(deletedEvaluation.getLevel(), deletedEvaluation.getComment());
        // Delete evaluation
        evaluationRepository.deleteById(evaluationId);
    }
}
