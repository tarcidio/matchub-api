package com.matchub.api.matchub_api.controller;

import com.matchub.api.matchub_api.dto.ScreenDTODetails;
import com.matchub.api.matchub_api.service.ScreenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Screen", description = "")
@RestController
@RequestMapping(value = "/screens")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ScreenDTODetails> findById(@PathVariable Long id){
        ScreenDTODetails screen = screenService.findById(id);
        return ResponseEntity.ok().body(screen);
    }

    @GetMapping(value = "/{spotlightId}/{opponentId}")
    public ResponseEntity<ScreenDTODetails> findByChampions(@PathVariable Long spotlightId,
                                                  @PathVariable Long opponentId){
        ScreenDTODetails screen = screenService.findByChampionsId(spotlightId, opponentId);
        return ResponseEntity.ok().body(screen);
    }
}
