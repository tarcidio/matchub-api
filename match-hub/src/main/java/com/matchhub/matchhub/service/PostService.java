package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Post;
import com.matchhub.matchhub.repository.PostRepository;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService{

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post findByChampionsId(Long spotlightId, Long opponentId) {
        Optional<Post> obj = Optional.ofNullable(postRepository.findBySpotlight_IdAndOpponent_Id(spotlightId, opponentId));
        return obj.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Spotlight Champion Id: "  + spotlightId + "." +
                        "Opponent Champion Id: "  + opponentId + "." +
                        "Type: " + Post.class.getName())
        );
    }

    public Post findById(Long id){
        Optional<Post> obj = postRepository.findById(id);
        return obj.orElseThrow( () -> new ObjectNotFoundException(
                "Object Not Found. " +
                        "Id: "  + id + "." +
                        "Type: " + Post.class.getName())
        );
    }
}
