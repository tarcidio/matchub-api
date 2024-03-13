package com.matchhub.matchhub.riot_api;

import com.matchhub.matchhub.riot_api.dto.LinkLoginRiotDTO;
import com.matchhub.matchhub.riot_api.dto.RiotAuthResponseDTO;
import com.matchhub.matchhub.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class RiotAuthService {
    private final WebClient webClient;

    // Provider URL to authorize
    private final String provider = "https://auth.riotgames.com";
    private final String tokenUrl = provider + "/token";
    private final String authorizeUrl = provider + "/authorize";

    // Use another url when leaving the testing phase
    // Callback URL
    private final String appBaseUrl = "http://localhost:3000";
    private final String appCallbackUrl = appBaseUrl + "/integrate";

    // Set clientId and clientSecret when the application is approved
    // Attributes to oauth2.0
    private final String clientId = "client_id";
    private final String clientSecret = "client_secret";

    public RiotAuthService(WebClient webClient) {
        this.webClient = webClient;
    }

    public LinkLoginRiotDTO getLink() {
        String link = authorizeUrl + "?redirect_uri=" +
                appCallbackUrl + "&client_id=" +
                clientId + "&response_type=code" + "&scope=openid";
        return LinkLoginRiotDTO.builder().link(link).build();
    }

    public Mono<RiotAuthResponseDTO> getRiotUserJwtToken(String authorizationCode) {
        return webClient.post()// Webclient POST type request
                .uri(tokenUrl)//Set the URI path to make POST
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))//Lambda function that receives a 'Header', setting "Authorization: Basic ..." header with clientId and clientSecret
                .bodyValue(Map.of(// Set request body like OAuth2.0 protocol
                        "grant_type", "authorization_code",
                        "code", authorizationCode,// Put authorizationCode
                        "redirect_uri", appCallbackUrl// Set "redirect_uri": URI used to callback (following OAuth2.0 protocol)
                ))
                .retrieve()// Signals that we are ready to process the response
                .bodyToMono(RiotAuthResponseDTO.class)// Converts the response body to Mono<RiotAuthResponseDTO>
                .onErrorMap(WebClientResponseException.class, // Handles errors that may occur during the request
                        ex -> new RuntimeException("Riot Authentication wasn't successful"));
        // WebClientResponseException` it will be mapped to a new instance of `ObjectNotFoundException`
        // Turning low-level errors into exceptions that are more meaningful
    }
}
