package com.matchub.api.matchub_api.controller;

import java.net.URI;
import java.security.Principal;

import com.matchub.api.matchub_api.dto.CommentDTOBase;
import com.matchub.api.matchub_api.dto.CommentDTODetails;
import com.matchub.api.matchub_api.dto.CommentDTOLinks;
import com.matchub.api.matchub_api.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Comment", description = "")
@RestController
@RequestMapping(value = "/screens/{screenId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentDTODetails> findById(@PathVariable Long commentId){
        CommentDTODetails comment = commentService.findById(commentId);
        return ResponseEntity.ok().body(comment);
    }

    /* Disabled: Screen already have comments collections */
//    @GetMapping
//    public ResponseEntity<List<Comment>> findAll(@PathVariable Long screenId)

    @PostMapping
    public ResponseEntity<CommentDTODetails> create(@PathVariable Long screenId,
                                                  @RequestBody CommentDTOBase comment,
                                                  Principal connectedHubUser){
        CommentDTODetails savedComment = commentService.save(screenId, comment, connectedHubUser);
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
                                                  @RequestBody CommentDTOBase comment,
                                                  Principal connectedHubUser){
        CommentDTOLinks updatedComment = commentService.update(screenId, commentId, comment, connectedHubUser);
        return ResponseEntity.ok().body(updatedComment);
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId,
                                       Principal connectedHubUser){
        commentService.delete(commentId, connectedHubUser);
        return ResponseEntity.noContent().build();
    }

}
