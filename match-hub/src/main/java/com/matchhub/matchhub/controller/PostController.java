package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.Post;
import com.matchhub.matchhub.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping(value = "/{spotlightId}/{opponentId}")
    public ResponseEntity<Post> findByChampions(@PathVariable Long spotlightId,
                                         @PathVariable Long opponentId){
        Post post = postService.findByChampionsId(spotlightId, opponentId);
        return ResponseEntity.ok().body(post);
    }
}
