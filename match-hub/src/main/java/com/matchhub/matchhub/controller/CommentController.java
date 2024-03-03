package com.matchhub.matchhub.controller;

import java.net.URI;
import java.util.List;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/screens/{screenId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    /* Disabled: Screen already have comments collections */
//    @GetMapping(value = "/{commentId}")
//    public ResponseEntity<Comment> findById(@PathVariable Long commentId){
//        Comment comment = commentService.findById(commentId);
//        return ResponseEntity.ok().body(comment);
//    }

    /* Disabled: Screen already have comments collections */
//    @GetMapping
//    public ResponseEntity<List<Comment>> findAll(@PathVariable Long screenId)

    @PostMapping
    public ResponseEntity<Comment> create(@PathVariable Long screenId,
                                          @RequestBody Comment comment){
        Comment savedComment = commentService.save(screenId, comment);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedComment);
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<Comment> update(@PathVariable Long screenId,
                                          @PathVariable Long commentId,
                                          @RequestBody Comment comment){
        Comment updatedComment = commentService.update(screenId, commentId, comment);
        return ResponseEntity.ok().body(updatedComment);
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
