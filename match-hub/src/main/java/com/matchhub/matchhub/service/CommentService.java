package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.domain.enums.Role;
import com.matchhub.matchhub.dto.CommentDTOBase;
import com.matchhub.matchhub.dto.CommentDTODetails;
import com.matchhub.matchhub.dto.CommentDTOLinks;
import com.matchhub.matchhub.repository.CommentRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ScreenService screenService;
    private final HubUserService hubUserService;
    private final ModelMapper modelMapper;

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
        // Return response
        return modelMapper.map(commentDomain, CommentDTODetails.class);
    }

    public CommentDTODetails save(Long screenId,
                                CommentDTOBase comment,
                                Principal connectedHubUser) {
        //Get Screen
        Screen screen = screenService.findDomainById(screenId);
        // Get User Logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Check if User is blocked
        if(logged.getBlocked()){
            throw new IllegalArgumentException("User is blocked.");
        }
        // Transfer information
        Comment saveComment = modelMapper.map(comment, Comment.class);
        //Set Id, Screen and User
        saveComment.setId(null);
        saveComment.setScreen(screen);
        saveComment.setHubUser(logged);
        return modelMapper.map(commentRepository.save(saveComment), CommentDTODetails.class);
    }

    //1. Check if the comment passed as an argument actually has the screenId and commentId passed as arguments
    //2. Retrieve the comment and verify if it truly belongs to that screen
    //3. Check if the authenticated hubUser is the same as the one in the retrieved comment
    public CommentDTOLinks update(Long screenId,
                                  Long commentId,
                                  CommentDTOBase comment,
                                  Principal connectedHubUser) {
        //Get comment
        Comment updatedComment = this.findDomainById(commentId);
        // Get User Logged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();

        // Check if comment exists in screen
        if(!updatedComment.getScreen().getId().equals(screenId)){
            throw new IllegalArgumentException("Update is incompatible with the indicated screen.");
        }
        //Check if comment belong to authenticated hubUser id
        if (!updatedComment.getHubUser().getId().equals(logged.getId())){
            throw new IllegalArgumentException("Update isn't allow.");
        }

        // Convert
        modelMapper.map(comment, updatedComment);
        // Return response in correct format
        return modelMapper.map(commentRepository.save(updatedComment),CommentDTOLinks.class);
    }

    public void delete(Long commentId, Principal connectedHubUser) {
        // Get the comment
        Comment deletedComment = this.findDomainById(commentId);
        // Get user connected
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();
        // Check if comment belong to authenticated hubUser id
        if (!deletedComment.getHubUser().getId().equals(logged.getId())){
            throw new IllegalArgumentException("Delete isn't allow.");
        }
        // Delete comment
        commentRepository.deleteById(commentId);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
