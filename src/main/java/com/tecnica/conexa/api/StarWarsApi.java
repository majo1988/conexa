package com.tecnica.conexa.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Log4j2
public class StarWarsApi {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StarWarsApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public JsonNode fetchStarWarsData(String endpoint) {
        String apiUrl = "https://swapi.dev/api/" + endpoint;

        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
            return objectMapper.readTree(response);
        } catch (Exception e) {
            log.error("Error fetching data from Star Wars API: ", e);
            return null;
        }
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void scheduledUpdateStarWarsData() {
        System.out.println("entra aca ");
        log.info("Actualizando datos de Star Wars...");
        JsonNode peopleData = fetchStarWarsData("people/");
        JsonNode filmsData = fetchStarWarsData("films/");
        JsonNode starshipsData = fetchStarWarsData("starships/");
        JsonNode vehiclesData = fetchStarWarsData("vehicles/");

        log.info("People Data: {}", peopleData);
        log.info("Films Data: {}", filmsData);
        log.info("Starships Data: {}", starshipsData);
        log.info("Vehicles Data: {}", vehiclesData);
    }
}
