package com.matchhub.matchhub.config;

import com.matchhub.matchhub.domain.*;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import com.matchhub.matchhub.domain.enums.Known;
import com.matchhub.matchhub.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class AppConfig {

    private final PostRepository postRepository;
    private final ChampionRepository championRepository;
    private final HubUserRepository hubUserRepository;
    private final CommentRepository commentRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public AppConfig(PostRepository postRepository, ChampionRepository championRepository,
                     HubUserRepository hubUserRepository, CommentRepository commentRepository,
                     EvaluationRepository evaluationRepository) {
        this.postRepository = postRepository;
        this.championRepository = championRepository;
        this.hubUserRepository = hubUserRepository;
        this.commentRepository = commentRepository;
        this.evaluationRepository = evaluationRepository;
    }

    //ModelMapper: automate the object mapping process
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner startDB() {
        return args -> {
            //Create Champions
            Champion Gragas = new Champion(null, "Gragas", null);
            Champion Darius = new Champion(null, "Darius", null);
            Champion Renekton = new Champion(null, "Renekton", null);
            Champion Janna = new Champion(null, "Janna", null);
            championRepository.saveAll(List.of(Gragas, Darius, Renekton, Janna));

            //Create User
            HubUser Tarcidio = new HubUser();
            Tarcidio.setNickname("Tarcidio");
            HubUser Augusto = new HubUser();
            Augusto.setNickname("Augusto");
            HubUser Gabriel = new HubUser();
            Gabriel.setNickname("Gabriel");
            hubUserRepository.saveAll(List.of(Tarcidio, Augusto, Gabriel));

            //Create Posts
            Post GragasVsDarius = new Post(null, Gragas, Darius, Known.MEDIUM, new TreeSet<>());
            Post DariusVsGragas = new Post(null, Darius, Gragas, Known.MEDIUM, new TreeSet<>());
            Post DariusVsRenekton = new Post(null, Darius, Renekton, Known.HIGH, new TreeSet<>());
            Post RenektonVsDarius = new Post(null, Renekton, Darius, Known.HIGH, new TreeSet<>());
            Post JannaVsGragas = new Post(null, Janna, Gragas, Known.LOW, new TreeSet<>());
            Post GragasVsJanna = new Post(null, Gragas, Janna, Known.LOW, new TreeSet<>());
            postRepository.saveAll(List.of(GragasVsDarius, DariusVsGragas,
                    DariusVsRenekton, RenektonVsDarius, JannaVsGragas, GragasVsJanna));

            //Create Comments
            // Post GragasVsDarius
            Comment TarcidioGragasVsDarius = new Comment(null, Tarcidio, GragasVsDarius, null, null,
                    "Gragas precisa jogar muito agressivo nesta rota",
                    new HashSet<>());
            Comment GabrielGragasVsDarius = new Comment(null, Gabriel, GragasVsDarius, null, null,
                    "Gragas precisa jogar recuado nesta rota",
                    new HashSet<>());
            // Post JannaVsGragas
            Comment TarcidioJannaVsGragas = new Comment(null, Tarcidio, JannaVsGragas, null, null,
                    "Janna tem vantagem",
                    new HashSet<>());
            Comment AugustoJannaVsGragas = new Comment(null, Augusto, JannaVsGragas, null, null,
                    "Janna não faz absolutamente nada",
                    new HashSet<>());
            // Post RenektonVsDarius
            Comment GabrielRenektonVsDarius = new Comment(null, Gabriel, RenektonVsDarius, null, null,
                    "Ambos precisar tem muito cuidado",
                    new HashSet<>());
            Comment AugustoRenektonVsDarius = new Comment(null, Augusto, RenektonVsDarius, null, null,
                    "Parta para cima level 2",
                    new HashSet<>());
            commentRepository.saveAll(List.of(TarcidioGragasVsDarius, GabrielGragasVsDarius,
                    TarcidioJannaVsGragas, AugustoJannaVsGragas, GabrielRenektonVsDarius,
                    AugustoRenektonVsDarius));

            //Create Evaluation
            Evaluation e1 = new Evaluation(null, Augusto, TarcidioGragasVsDarius, EvaluationLevel.BAD, null, null);
            Evaluation e2 = new Evaluation(null, Augusto, GabrielGragasVsDarius, EvaluationLevel.GOOD, null, null);
            evaluationRepository.saveAll(List.of(e1,e2));
        };
    }
}
