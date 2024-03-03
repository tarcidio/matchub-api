package com.matchhub.matchhub.controller;

import com.matchhub.matchhub.domain.Screen;
import com.matchhub.matchhub.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/screens")
public class ScreenController {

    private final ScreenService screenService;

    @Autowired
    public ScreenController(ScreenService screenService){
        this.screenService = screenService;
    }

    @GetMapping(value = "/{spotlightId}/{opponentId}")
    public ResponseEntity<Screen> findByChampions(@PathVariable Long spotlightId,
                                                  @PathVariable Long opponentId){
        Screen screen = screenService.findByChampionsId(spotlightId, opponentId);
        return ResponseEntity.ok().body(screen);
    }
}
