package com.matchhub.matchhub.controller;

import java.net.URI;
import java.util.List;
import com.matchhub.matchhub.domain.Comment;
import com.matchhub.matchhub.dto.CommentDTOBase;
import com.matchhub.matchhub.dto.CommentDTODetails;
import com.matchhub.matchhub.dto.CommentDTOLinks;
import com.matchhub.matchhub.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Comment", description = "")
@RestController
@RequestMapping(value = "/screens/{screenId}/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentDTODetails> findById(@PathVariable Long commentId){
        CommentDTODetails comment = commentService.findById(commentId);
        return ResponseEntity.ok().body(comment);
    }

    /* Disabled: Screen already have comments collections */
//    @GetMapping
//    public ResponseEntity<List<Comment>> findAll(@PathVariable Long screenId)

    @PostMapping
    public ResponseEntity<CommentDTOLinks> create(@PathVariable Long screenId,
                                          @RequestBody CommentDTOBase comment){
        CommentDTOLinks savedComment = commentService.save(screenId, comment);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedComment.getId())
                .toUri();
        return ResponseEntity.created(uri).body(savedComment);
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<CommentDTOLinks> update(@PathVariable Long screenId,
                                          @PathVariable Long commentId,
                                          @RequestBody CommentDTOBase comment){
        CommentDTOLinks updatedComment = commentService.update(screenId, commentId, comment);
        return ResponseEntity.ok().body(updatedComment);
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }

}
