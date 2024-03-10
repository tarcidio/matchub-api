package com.matchhub.matchhub.config;

import com.matchhub.matchhub.domain.*;
import com.matchhub.matchhub.domain.enums.EvaluationLevel;
import com.matchhub.matchhub.domain.enums.Known;
import com.matchhub.matchhub.domain.enums.Role;
import com.matchhub.matchhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final PasswordEncoder passwordEncoder;
    private final ScreenRepository screenRepository;
    private final ChampionRepository championRepository;
    private final HubUserRepository hubUserRepository;
    private final CommentRepository commentRepository;
    private final EvaluationRepository evaluationRepository;

    //ModelMapper: automate the object mapping process
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CommandLineRunner startDB() {
        return args -> {
            //Create Users
            HubUser Tarcidio = HubUser.builder().
                    nickname("tarca").
                    firstname("tarcidio").
                    lastname("antonio").
                    email("tarcidio@gmail.com").
                    username("tarcidio").
                    blocked(false).
                    password(passwordEncoder.encode("123")).
                    role(Role.ADMIN).
                    build();
            HubUser Augusto = HubUser.builder().
                    nickname("Augusto").
                    email("augusto@gmail.com").
                    username("augusto").
                    blocked(false).
                    password(passwordEncoder.encode("123")).
                    role(Role.MODERATOR).
                    build();
            HubUser Gabriel = HubUser.builder().
                    nickname("Gabriel").
                    email("gabriel@gmail.com").
                    username("gabriel").
                    blocked(false).
                    password(passwordEncoder.encode("123")).
                    role(Role.HUBUSER).
                    build();
            hubUserRepository.saveAll(List.of(Tarcidio, Augusto, Gabriel));

            //Create Champions
            Champion Gragas = Champion.builder().name("Gragas").build();
            Champion Darius = Champion.builder().name("Darius").build();
            Champion Renekton = Champion.builder().name("Renekton").build();
            Champion Janna = Champion.builder().name("Janna").build();
            championRepository.saveAll(List.of(Gragas, Darius, Renekton, Janna));

            //Create Screens
            Screen GragasVsDarius = Screen.builder().
                    spotlight(Gragas).
                    opponent(Darius).
                    known(Known.MEDIUM).
                    build();
            Screen DariusVsGragas = Screen.builder().
                    spotlight(Darius).
                    opponent(Gragas).
                    known(Known.MEDIUM).
                    build();
            Screen DariusVsRenekton = Screen.builder().
                    spotlight(Darius).
                    opponent(Renekton).
                    known(Known.HIGH).
                    build();
            Screen RenektonVsDarius = Screen.builder().
                    spotlight(Renekton).
                    opponent(Darius).
                    known(Known.HIGH).
                    build();
            Screen JannaVsGragas = Screen.builder().
                    spotlight(Janna).
                    opponent(Gragas).
                    known(Known.LOW).
                    build();
            Screen GragasVsJanna = Screen.builder().
                    spotlight(Gragas).
                    opponent(Janna).
                    known(Known.LOW).
                    build();
            screenRepository.saveAll(List.of(GragasVsDarius, DariusVsGragas,
                    DariusVsRenekton, RenektonVsDarius, JannaVsGragas, GragasVsJanna));

            // Create Comments
            // Screen GragasVsDarius
            Comment TarcidioGragasVsDarius = Comment.builder().
                    hubUser(Tarcidio).
                    screen(GragasVsDarius).
                    text("Gragas precisa jogar muito agressivo nesta rota").
                    build();
            Comment GabrielGragasVsDarius = Comment.builder().
                    hubUser(Gabriel).
                    screen(GragasVsDarius).
                    text("Gragas precisa jogar recuado nesta rota").
                    build();
            // Screen JannaVsGragas
            Comment TarcidioJannaVsGragas = Comment.builder().
                    hubUser(Tarcidio).
                    screen(JannaVsGragas).
                    text("Janna tem vantagem").
                    build();
            Comment AugustoJannaVsGragas = Comment.builder().
                    hubUser(Augusto).
                    screen(JannaVsGragas).
                    text("Janna não faz absolutamente nada").
                    build();
            // Screen RenektonVsDarius
            Comment GabrielRenektonVsDarius = Comment.builder().
                    hubUser(Gabriel).
                    screen(RenektonVsDarius).
                    text("Ambos precisar tem muito cuidado").
                    build();
            Comment AugustoRenektonVsDarius = Comment.builder().
                    hubUser(Augusto).
                    screen(RenektonVsDarius).
                    text("Parta para cima level 2").
                    build();
            commentRepository.saveAll(List.of(TarcidioGragasVsDarius, GabrielGragasVsDarius,
                    TarcidioJannaVsGragas, AugustoJannaVsGragas, GabrielRenektonVsDarius,
                    AugustoRenektonVsDarius));

            //Create Evaluation
            Evaluation e1 = Evaluation.builder().
                    hubUser(Augusto).
                    comment(TarcidioGragasVsDarius).
                    level(EvaluationLevel.BAD).
                    build();
            Evaluation e2 = Evaluation.builder().
                    hubUser(Augusto).
                    comment(GabrielGragasVsDarius).
                    level(EvaluationLevel.GOOD).
                    build();
            evaluationRepository.saveAll(List.of(e1,e2));
        };
    }


}
