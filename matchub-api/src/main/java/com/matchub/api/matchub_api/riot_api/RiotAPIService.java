package com.matchub.api.matchub_api.riot_api;

import com.matchub.api.matchub_api.domain.Champion;
import com.matchub.api.matchub_api.domain.HubUser;
import com.matchub.api.matchub_api.domain.enums.Continent;
import com.matchub.api.matchub_api.domain.enums.Region;
import com.matchub.api.matchub_api.dto.HubUserDTOLinks;
import com.matchub.api.matchub_api.repository.HubUserRepository;
import com.matchub.api.matchub_api.riot_api.dto.RiotAuthResponseDTO;
import com.matchub.api.matchub_api.riot_api.dto.SummonerDTO;
import com.matchub.api.matchub_api.riot_api.dto.TopMasteryDTO;
import com.matchub.api.matchub_api.service.ChampionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;

@Service
public class RiotAPIService {
    @Value("${riot.development.api.key}")
    private String riotDevelopmentAPIKey;

    private final WebClient webClient;

    private final ChampionService championService;

    private final ModelMapper modelMapper;

    private final HubUserRepository hubUserRepository;

    public RiotAPIService(WebClient webClient, RiotAuthService riotAuthService, ChampionService championService, ModelMapper modelMapper, HubUserRepository hubUserRepository) {
        this.webClient = webClient;
        this.championService = championService;
        this.modelMapper = modelMapper;
        this.hubUserRepository = hubUserRepository;
    }

    public Mono<SummonerDTO> getRiotUserByJwtToken(String jwtToken, String continent) {
        /* I don't know which url is */
        //url1 : "https://auth.riotgames.com/userinfo";//UserInfoResponseDTO
        //url2 : "https://americas.api.riotgames.com/riot/account/v1/accounts/me";//SummonerDTO

        URI uri = UriComponentsBuilder
                .fromUriString("https://{continent}.api.riotgames.com/riot/account/v1/accounts/me")
                .queryParam(continent)
                .build().toUri();

        return webClient.get()
                .uri(uri)
                .headers(headers -> headers.setBearerAuth(jwtToken))
                .retrieve()
                .bodyToMono(SummonerDTO.class)
                .onErrorMap(WebClientResponseException.class,
                        ex -> new RuntimeException("Riot Authentication was successful, but it can't get summoner id"));
    }

    public Mono<TopMasteryDTO> getTopSummonerMastery(String puuid, String region) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://{region}.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}/top")
                .queryParam("count", "1")
                .build(region, puuid);

        return webClient.get()
                .uri(uri)
                .header("X-Riot-Token", riotDevelopmentAPIKey)
                .retrieve()
                .bodyToMono(TopMasteryDTO.class)
                .onErrorMap(WebClientResponseException.class, ex -> new RuntimeException("Failed to fetch top summoner mastery"));
    }

    public HubUserDTOLinks integrateHubUserAndSummoner(String authorizationCode,
                                                       Principal connectedHubUser,
                                                       RiotAuthResponseDTO RiotAuthResponseDTO){
        /* Future improvements: remove blocks and use reactive programming */

        // Get HubUser looged
        HubUser logged = (HubUser) ((UsernamePasswordAuthenticationToken) connectedHubUser).getPrincipal();

        // Get Continent and Region
        // Fix: nullable
        Region region = logged.getRegion();
        Continent continent = region.getContinent();

        // Get Summoner Identifiers
        Mono<SummonerDTO> MonoSummoner = this.getRiotUserByJwtToken(RiotAuthResponseDTO.getAccess_token(), continent.toString().toLowerCase());
        SummonerDTO summoner = MonoSummoner.block();
        // Get Mastery Data
        Mono<TopMasteryDTO> MonoTopMastery = this.getTopSummonerMastery(summoner.getPuuid(), region.toString().toLowerCase());
        TopMasteryDTO TopMastery = MonoTopMastery.block();

        // Set Data
        Champion mastery = championService.findDomainById(TopMastery.getChampionId());
        logged.setMastery(mastery);
        logged.setAbilityLevelByPoints(TopMastery.getChampionPoints());
        logged.setSummonerName(summoner.getName());

        // Save data
        return modelMapper.map(hubUserRepository.save(logged), HubUserDTOLinks.class);
    }
}
