package com.tecnica.conexa.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j2
@Api(tags = "StarWars API")
public class StarWarsApi {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public StarWarsApi(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(value = "Obtener datos de Star Wars", notes = "Obtiene datos de la API de Star Wars basados en el endpoint proporcionado.")
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
}
