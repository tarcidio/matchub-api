package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Post;
import com.matchhub.matchhub.repository.CommentRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final PostService postService;

    private final HubUserService hubUserService;

    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService, HubUserService hubUserService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postService = postService;
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

    public Comment save(Long postId, Comment comment) {
        //Get Post
        Post post = postService.findById(postId);
        //Get HubUser
        //1. Autenticate
        //2. Get hubUserId
        //3. Use HubUserService to get object
        // Provisional:
        HubUser hubUser = new HubUser();
        hubUser.setId(comment.getHubUser().getId());

        //Validate Post and User
        if (comment.getPost() != null && !comment.getPost().getId().equals(post.getId())) {
            //Customize exception
            throw new IllegalArgumentException("Comment is incompatible with the indicated post.");
        }

        //Set Id, Post and User
        comment.setId(null);
        comment.setPost(post);
        comment.setHubUser(hubUser);

        return commentRepository.save(comment);
    }

    //duvida: post id eh necessario? eu nao preciso checar se esse comentario eh relametne deste post?
    //no update esse check é mais importante (comment id e o que ta no body?)
    //check no post id tambem
    //nao tem a necessidade de recuperar as entidade user e post
    public Comment update(Long postId, Long commentId, Comment comment) {
        //Get HubUser
        //1. Autenticate
        //2. Get hubUserId

        //Validate Id, Post and User with new comment
        if (comment.getPost() != null && !comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Comment is incompatible with the indicated post.");
        }

        if (!comment.getId().equals(commentId)) {
            throw new IllegalArgumentException("Comment Id is incompatible with the indicated.");
        }

        //Validate Id, Post and User with old comment

        Comment updatedComment = this.findById(comment.getId());
        modelMapper.map(comment, updatedComment);
        return commentRepository.save(updatedComment);
    }

    public void delete(Long postId, Long commentId) {
        //validacao
    }
}
