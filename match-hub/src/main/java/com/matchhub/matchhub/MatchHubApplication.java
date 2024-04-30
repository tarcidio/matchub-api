package com.matchhub.matchhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchHubApplication {

	// To use: http://localhost:8080/swagger-ui/index.html
	// To use: http://localhost:8080/h2-console

	// Explain documentation: https://developer.riotgames.com/docs/lol
	// Champions JSON: https://ddragon.leagueoflegends.com/cdn/14.5.1/data/en_US/champion.json
	// Champion JSON: https://ddragon.leagueoflegends.com/cdn/14.5.1/data/en_US/champion/Aatrox.json
	// API LoL for Developers: https://developer.riotgames.com/apis
	public static void main(String[] args) {
		SpringApplication.run(MatchHubApplication.class, args);
	}

}
