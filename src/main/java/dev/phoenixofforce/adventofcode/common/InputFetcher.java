package dev.phoenixofforce.adventofcode.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class InputFetcher {

    @Value("${aoc.session}")
    private String sessionToken;

    public String getInputFromSite(int day, int year) {
        log.info("Fetching Input from Site...");
        if(sessionToken.isEmpty()) {
            log.warn("Session token is empty");
            return "";
        } else {
            log.info("Found session token");
        }


        WebClient client = WebClient.create();

        WebClient.ResponseSpec responseSpec = client.post()
                .uri("https://adventofcode.com/" + year + "/day/" + day + "/input")
                .header("User-Agent", "Private Tool from phoenixofforce@gmail.com")
                .cookie("session", sessionToken)
                .retrieve();

        try {
            ResponseEntity<String> response = responseSpec
                    .toEntity(String.class)
                    .onErrorResume(e -> null)
                    .block();

            if(response != null && response.getStatusCode().is2xxSuccessful()) return response.getBody();
        } catch(Exception ignored) { }
        log.error("Failed to fetch input");

        return "";
    }

}
