package com.matchhub.matchhub.service;

import com.matchhub.matchhub.domain.Post;
import com.matchhub.matchhub.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;

    @BeforeEach
    void setUp() {
        //startPost();
    }

    private void startPost() {
        //post = new Post();
    }
}