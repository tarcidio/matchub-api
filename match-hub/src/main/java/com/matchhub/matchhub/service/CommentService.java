package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.dto.*;
import com.matchhub.matchhub.repository.CommentRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ScreenService screenService;

    private final HubUserService hubUserService;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          ScreenService screenService,
                          HubUserService hubUserService,
                          ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.screenService = screenService;
        this.hubUserService = hubUserService;
        this.modelMapper = modelMapper;
    }

    public Comment findDomainById(Long id){
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + Comment.class.getName()));
    }

    public CommentDTODetails findById(Long id) {
        // Get information and check existence
        Optional<Comment> comment = commentRepository.findById(id);
        Comment commentDomain = comment.orElseThrow(() -> new ObjectNotFoundException(
                "Object Not Found. " +
                "Id: "  + id + "." +
                "Type: " + Comment.class.getName()));
        // Converter
        CommentDTODetails commentDTODetails = new CommentDTODetails();
        modelMapper.map(commentDomain, commentDTODetails);
        return commentDTODetails;
    }

    public CommentDTOLinks save(Long screenId, CommentDTOBase comment) {
        //Get Screen
        Screen screen = screenService.findDomainById(screenId);

        // Get User Logged
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        HubUser hubUser = null;
//        if (authentication != null && authentication.getPrincipal() instanceof User hubUserDetails) {
//            String hubUserUsername = hubUserDetails.getUsername();
//            hubUser = hubUserService.findByUsername(hubUserUsername);
//        }
        HubUser hubUser = new HubUser();
        hubUser.setId(2L);



        // Create repository instance
        Comment saveComment = new Comment();
        // Convert
        modelMapper.map(comment, saveComment);
        //Set Id, Screen and User
        saveComment.setId(null);
        saveComment.setScreen(screen);
        saveComment.setHubUser(hubUser);
        return modelMapper.map(commentRepository.save(saveComment), CommentDTOLinks.class);
    }

    //1. Check if the comment passed as an argument actually has the screenId and commentId passed as arguments
    //2. Retrieve the comment and verify if it truly belongs to that screen
    //3. Check if the authenticated hubUser is the same as the one in the retrieved comment
    public CommentDTOLinks update(Long screenId, Long commentId, CommentDTOBase comment) {
        //Get comment
        Comment updatedComment = this.findDomainById(commentId);
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

        // Convert
        modelMapper.map(comment, updatedComment);
        // Return response in correct format
        return modelMapper.map(commentRepository.save(updatedComment),CommentDTOLinks.class);
    }

    public void delete(Long commentId) {
        /*Need authentication*/
        //Validate Id, Screen and User
        commentRepository.deleteById(commentId);
    }
}
