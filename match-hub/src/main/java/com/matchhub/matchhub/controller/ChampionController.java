package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.dto.ChampionDTODetails;
import com.matchhub.matchhub.dto.ScreenDTODetails;
import com.matchhub.matchhub.service.ChampionService;
import com.matchhub.matchhub.service.ScreenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Champion", description = "")
@RestController
@RequestMapping(value = "/champions")
public class ChampionController {
    private final ChampionService championService;

    @Autowired
    public ChampionController(ChampionService championService){
        this.championService = championService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChampionDTODetails> findById(@PathVariable Long id){
        ChampionDTODetails championDTODetails = championService.findById(id);
        return ResponseEntity.ok().body(championDTODetails);
    }
}
