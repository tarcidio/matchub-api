package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.dto.ScreenDTODetails;
import com.matchhub.matchhub.service.ScreenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Screen", description = "")
@RestController
@RequestMapping(value = "/screens")
public class ScreenController {

    private final ScreenService screenService;

    @Autowired
    public ScreenController(ScreenService screenService){
        this.screenService = screenService;
    }

    @GetMapping(value = "/{spotlightId}/{opponentId}")
    public ResponseEntity<ScreenDTODetails> findByChampions(@PathVariable Long spotlightId,
                                                  @PathVariable Long opponentId){
        ScreenDTODetails screen = screenService.findByChampionsId(spotlightId, opponentId);
        return ResponseEntity.ok().body(screen);
    }
}
