package com.matchhub.matchhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchHubApplication {

	// To use: http://localhost:8080/swagger-ui/index.html
	// To use: http://localhost:8080/h2-console
	public static void main(String[] args) {
		SpringApplication.run(MatchHubApplication.class, args);
	}

}
