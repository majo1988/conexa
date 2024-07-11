package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.entity.Starships;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Api(tags = "Starships Service")
public class StarshipService {
    @Autowired
    private StarWarsApi starWarsApi;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private FilmsService filmsService;

    private int currentId = 1;
    /**
     * Obtiene una lista de starships desde la API de Star Wars.
     *
     * @param endpoint El endpoint de la API de Star Wars para las starships.
     * @return Una lista de objetos Starships que representan las starships obtenidas.
     */
    @ApiOperation(value = "Obtener starships de Star Wars", notes = "Obtiene una lista de starships desde la API de Star Wars.")
    public List<Starships> obtenerStarships(String endpoint) {
        JsonNode starshipsData = starWarsApi.fetchStarWarsData(endpoint);
        List<Starships> starshipsList = new ArrayList<>();

        if (starshipsData != null && starshipsData.has("results")) {
            List<CompletableFuture<Starships>> futures = new ArrayList<>();
            Iterator<JsonNode> elements = starshipsData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode starshipNode = elements.next();
                CompletableFuture<Starships> future = CompletableFuture.supplyAsync(() -> {
                    Starships starship = new Starships();
                    starship.setId(currentId++);
                    starship.setName(starshipNode.get("name").asText());
                    starship.setModel(starshipNode.get("model").asText());
                    starship.setStarshipClass(starshipNode.get("starship_class").asText());
                    starship.setManufacturer(starshipNode.get("manufacturer").asText());
                    starship.setCostInCredits(starshipNode.get("cost_in_credits").asText());
                    starship.setLength(starshipNode.get("length").asText());
                    starship.setCrew(starshipNode.get("crew").asText());
                    starship.setPassengers(starshipNode.get("passengers").asText());
                    starship.setCargoCapacity(starshipNode.get("cargo_capacity").asText());
                    starship.setConsumables(starshipNode.get("consumables").asText());
                    starship.setHyperdriveRating(starshipNode.get("hyperdrive_rating").asText());
                    starship.setMglt(starshipNode.get("MGLT").asText());
                    starship.setMaxAtmospheringSpeed(starshipNode.get("max_atmosphering_speed").asText());

                    List<People> pilots = peopleService.getCharacters(starshipNode.get("pilots"));
                    starship.setPilots(pilots);
                    List<Films> films = filmsService.getFilms(starshipNode.get("pilots"));
                    starship.setPilots(pilots);

                    starship.setUrl(starshipNode.get("url").asText());

                    return starship;
                });
                futures.add(future);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[futures.size()]));

            CompletableFuture<List<Starships>> allStarshipsFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));

            try {
                starshipsList = allStarshipsFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return starshipsList;
    }
    /**
     * Convierte un nodo JSON de URLs en una lista de Strings.
     *
     * @param jsonNode Nodo JSON que contiene URLs.
     * @return Una lista de URLs como Strings.
     */
    private List<String> getUrlsAsStringList(JsonNode jsonNode) {
        List<String> list = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                list.add(node.asText());
            }
        }
        return list;
    }

    /**
     * Obtiene una lista de starships filtradas por URLs de la API de Star Wars.
     *
     * @param filmsNode Nodo JSON que contiene URLs de starships.
     * @return Una lista de objetos Starships que coinciden con las URLs proporcionadas.
     */
    @ApiOperation(value = "Obtener starships filtradas por URLs", notes = "Obtiene una lista de starships filtradas por URLs de la API de Star Wars.")

    public List<Starships> getStarships(JsonNode filmsNode) {
        List<Starships> starships = new ArrayList<>();
        if (filmsNode != null && filmsNode.isArray()) {
            List<Starships> filmList = obtenerComparatorStarships("starships/");
            for (JsonNode filmsUrlNode : filmsNode) {
                String starshipsUrl = filmsUrlNode.asText();
                for (Starships starship : filmList) {
                    if (starshipsUrl.equals(starship.getUrl())) {
                        starships.add(starship);
                        break;
                    }
                }
            }
        }
        return starships;
    }
    /**
     * Obtiene una lista simplificada de starships para comparación.
     *
     * @param endpoint El endpoint de la API de Star Wars para las starships.
     * @return Una lista de objetos Starships simplificados para comparación.
     */
    @ApiOperation(value = "Obtener starships simplificadas para comparación", notes = "Obtiene una lista simplificada de starships desde la API de Star Wars.")

    public List<Starships> obtenerComparatorStarships(String endpoint) {

        JsonNode starshipsData = starWarsApi.fetchStarWarsData(endpoint);
        List<Starships> starshipsList = new ArrayList<>();

        if (starshipsData != null && starshipsData.has("results")) {
            Iterator<JsonNode> elements = starshipsData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode starshipNode = elements.next();
                Starships starship = new Starships();
                starship.setId(currentId++);
                starship.setName(starshipNode.get("name").asText());
                starship.setUrl(starshipNode.get("url").asText());
                starshipsList.add(starship);
            }
        }

        return starshipsList;
    }
}
