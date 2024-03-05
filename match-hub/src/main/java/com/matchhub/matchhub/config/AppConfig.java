package com.matchhub.matchhub.config;

import com.matchhub.matchhub.domain.*;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import com.matchhub.matchhub.domain.enums.Known;
import com.matchhub.matchhub.repository.*;
import io.swagger.v3.oas.models.OpenAPI;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class AppConfig {

    private final ScreenRepository screenRepository;
    private final ChampionRepository championRepository;
    private final HubUserRepository hubUserRepository;
    private final CommentRepository commentRepository;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public AppConfig(ScreenRepository screenRepository, ChampionRepository championRepository,
                     HubUserRepository hubUserRepository, CommentRepository commentRepository,
                     EvaluationRepository evaluationRepository) {
        this.screenRepository = screenRepository;
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
            Tarcidio.setEmail("tarcidio@gmail.com");
            Tarcidio.setLogin("tarcidio");
            HubUser Augusto = new HubUser();
            Augusto.setNickname("Augusto");
            Augusto.setEmail("augusto@gmail.com");
            Augusto.setLogin("augusto");
            HubUser Gabriel = new HubUser();
            Gabriel.setNickname("Gabriel");
            Gabriel.setEmail("gabriel@gmail.com");
            Gabriel.setLogin("gabriel");
            hubUserRepository.saveAll(List.of(Tarcidio, Augusto, Gabriel));

            //Create Screens
            Screen GragasVsDarius = new Screen(null, Gragas, Darius, Known.MEDIUM, new ArrayList<>());
            Screen DariusVsGragas = new Screen(null, Darius, Gragas, Known.MEDIUM, new ArrayList<>());
            Screen DariusVsRenekton = new Screen(null, Darius, Renekton, Known.HIGH, new ArrayList<>());
            Screen RenektonVsDarius = new Screen(null, Renekton, Darius, Known.HIGH, new ArrayList<>());
            Screen JannaVsGragas = new Screen(null, Janna, Gragas, Known.LOW, new ArrayList<>());
            Screen GragasVsJanna = new Screen(null, Gragas, Janna, Known.LOW, new ArrayList<>());
            screenRepository.saveAll(List.of(GragasVsDarius, DariusVsGragas,
                    DariusVsRenekton, RenektonVsDarius, JannaVsGragas, GragasVsJanna));

            //Create Comments
            // Screen GragasVsDarius
            Comment TarcidioGragasVsDarius = new Comment(null, Tarcidio, GragasVsDarius,
                    null, null, null, null,
                    "Gragas precisa jogar muito agressivo nesta rota",
                    new ArrayList<>());
            Comment GabrielGragasVsDarius = new Comment(null, Gabriel, GragasVsDarius,
                    null, null, null, null,
                    "Gragas precisa jogar recuado nesta rota",
                    new ArrayList<>());
            // Screen JannaVsGragas
            Comment TarcidioJannaVsGragas = new Comment(null, Tarcidio, JannaVsGragas,
                    null, null, null, null,
                    "Janna tem vantagem",
                    new ArrayList<>());
            Comment AugustoJannaVsGragas = new Comment(null, Augusto, JannaVsGragas,
                    null, null, null, null,
                    "Janna não faz absolutamente nada",
                    new ArrayList<>());
            // Screen RenektonVsDarius
            Comment GabrielRenektonVsDarius = new Comment(null, Gabriel, RenektonVsDarius,
                    null, null, null, null,
                    "Ambos precisar tem muito cuidado",
                    new ArrayList<>());
            Comment AugustoRenektonVsDarius = new Comment(null, Augusto, RenektonVsDarius,
                    null, null, null, null,
                    "Parta para cima level 2",
                    new ArrayList<>());
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
