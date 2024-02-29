package com.matchhub.matchhub.config;

import com.matchhub.matchhub.domain.Champion;
import com.matchhub.matchhub.domain.HubUser;
import com.matchhub.matchhub.domain.Post;
import com.matchhub.matchhub.domain.enums.Known;
import com.matchhub.matchhub.repository.ChampionRepository;
import com.matchhub.matchhub.repository.HubUserRepository;
import com.matchhub.matchhub.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {

    private final PostRepository postRepository;
    private final ChampionRepository championRepository;
    private final HubUserRepository hubUserRepository;

    @Autowired
    public AppConfig(PostRepository postRepository, ChampionRepository championRepository, HubUserRepository hubUserRepository) {
        this.postRepository = postRepository;
        this.championRepository = championRepository;
        this.hubUserRepository = hubUserRepository;
    }

    //ModelMapper: automate the object mapping process
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner startDB() {
        return args -> {
            Champion c1 = new Champion();
            Champion c2 = new Champion();
            championRepository.saveAll(List.of(c1, c2));
            Post post1 = new Post();
            post1.setSpotlight(c1);
            post1.setOpponent(c2);
            post1.setKnown(Known.LOW);
            postRepository.save(post1);

            HubUser hubUser1 = new HubUser();
            HubUser hubUser2 = new HubUser();
            hubUserRepository.saveAll(List.of(hubUser1, hubUser2));
        };
    }
}
