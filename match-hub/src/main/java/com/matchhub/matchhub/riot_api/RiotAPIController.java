package com.matchhub.matchhub.riot_api;

import com.matchhub.matchhub.dto.HubUserDTOLinks;
import com.matchhub.matchhub.riot_api.dto.LinkLoginRiotDTO;
import com.matchhub.matchhub.riot_api.dto.RiotAuthResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@Tag(name = "Authentication Riot User", description = "")
@RequestMapping("/rso")
@RequiredArgsConstructor
@Hidden
public class RiotAPIController {
    /* This Controller isn't working because it's necessary register product */

    private final RiotAPIService riotAPIService;

    private final RiotAuthService riotAuthService;

    @GetMapping("/link")
    public ResponseEntity<LinkLoginRiotDTO> getLink() {
        return ResponseEntity.ok().body(riotAuthService.getLink());
    }

    // It's usefully to refresh summoner data
    @GetMapping("/integrate")
    //@RequestParam because RSO return data in URL
    public ResponseEntity<HubUserDTOLinks> integrate(
            @RequestParam("code") String authorizationCode,
            Principal connectedHubUser) {
        // OAuth2.0
        Mono<RiotAuthResponseDTO> MonoJwtTokens = riotAuthService.getRiotUserJwtToken(authorizationCode);
        // Send JwtToken, get and set data
        HubUserDTOLinks updatedHubUser = riotAPIService.integrateHubUserAndSummoner(authorizationCode,
                connectedHubUser, MonoJwtTokens.block());
        return ResponseEntity.ok().body(updatedHubUser);
    }

    /* Obs: Since we don't need the user to be constantly fetching information
     * throughout the use of the application, we don't need to attach the LoL token to their tuple in the database.
     * But if one day we need to, we would have to attach or create a table for Riot tokens. Therefore,
     * we would need to create a controller just to get the token and create other controllers
     * to keep accessing the LoL API */

}
