package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.repository.CommentRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ScreenService screenService;

    private final HubUserService hubUserService;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, ScreenService screenService, HubUserService hubUserService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.screenService = screenService;
        this.hubUserService = hubUserService;
        this.modelMapper = modelMapper;
    }

    public Comment findById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Comment.class.getName()));
    }

    public Comment save(Long screenId, Comment comment) {
        //Get Screen
        Screen screen = screenService.findById(screenId);
        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId
        //3. Use HubUserService to get object
        // Provisional:
        HubUser hubUser = new HubUser();
        hubUser.setId(comment.getHubUser().getId());

        //Validate new comment with screen id
        if (comment.getScreen() != null && !comment.getScreen().getId().equals(screen.getId())) {
            //Customize exception
            throw new IllegalArgumentException("Comment is incompatible with the indicated screen.");
        }
        //Validate new comment with authenticated hubUser id
//        if (comment.getHubUser() != null && !comment.getHubUser().getId().equals(hubUser.getId())) {
//            //Customize exception
//            throw new IllegalArgumentException("Comment is incompatible with the indicated screen.");
//        }

        //Set Id, Screen and User
        comment.setId(null);
        comment.setScreen(screen);
        comment.setHubUser(hubUser);

        return commentRepository.save(comment);
    }

    //1. Check if the comment passed as an argument actually has the screenId and commentId passed as arguments
    //2. Retrieve the comment and verify if it truly belongs to that screen
    //3. Check if the authenticated hubUser is the same as the one in the retrieved comment
    public Comment update(Long screenId, Long commentId, Comment comment) {
        //Get comment
        Comment updatedComment = this.findById(commentId);
        //Get HubUser
        //1. Authenticate
        //2. Get hubUserId

        //Check if comment exists in screen
        if(!updatedComment.getScreen().getId().equals(screenId)){
            throw new IllegalArgumentException("Update is incompatible with the indicated screen.");
        }
        //Check if comment belong to authenticated hubUser id
//      if (updatedComment.getHubUser().getId().equals(hubUser.getId())){
//          throw new IllegalArgumentException("Update isn't allow.");
//      }

        //Validate new body comment with screen id
        if (comment.getScreen() != null && !comment.getScreen().getId().equals(screenId)) {
            throw new IllegalArgumentException("Comment is incompatible with the indicated screen.");
        }

        //Validate new body comment with authenticate hubUser id
//        if (comment.getHubUser() != null && !comment.getHubUser().getId().equals(hubUser.getId())){
//            throw new IllegalArgumentException("Comment is incompatible with the indicated screen.");
//        }

        //No make sense, because comment don't have id (it's null)
//        if (!comment.getId().equals(commentId)) {
//            throw new IllegalArgumentException("Comment Id is incompatible with the indicated.");
//        }

        modelMapper.map(comment, updatedComment);
        return commentRepository.save(updatedComment);
    }

    public void delete(Long commentId) {
        /*Need authentication*/
        //Validate Id, Screen and User
        commentRepository.deleteById(commentId);
    }
}
