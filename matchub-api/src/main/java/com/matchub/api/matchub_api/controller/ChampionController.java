package com.matchub.api.matchub_api.controller;

import com.matchub.api.matchub_api.dto.ChampionDTODetails;
import com.matchub.api.matchub_api.service.ChampionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Champion", description = "")
@RestController
@RequestMapping(value = "/champions")
@RequiredArgsConstructor
public class ChampionController {
    private final ChampionService championService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChampionDTODetails> findById(@PathVariable Long id){
        ChampionDTODetails championDTODetails = championService.findById(id);
        return ResponseEntity.ok().body(championDTODetails);
    }

    @GetMapping
    public ResponseEntity<List<ChampionDTODetails>> findAll(){
        List<ChampionDTODetails> championDTODetails = championService.findAll();
        return ResponseEntity.ok().body(championDTODetails);
    }
}
