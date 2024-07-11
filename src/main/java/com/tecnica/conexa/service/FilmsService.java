package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.entity.Vehicles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Api(tags = "Films Service")
public class FilmsService {

    @Autowired
    private StarWarsApi starWarsApi;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private VehiclesService vehiclesService;


    @Autowired
    private StarshipService starshipService;


    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Obtiene una lista de películas desde la API de Star Wars.
     *
     * @param endpoint El endpoint de la API de Star Wars para las películas.
     * @return Una lista de objetos Films que representan las películas obtenidas.
     */
    @ApiOperation(value = "Obtener películas de Star Wars", notes = "Obtiene una lista de películas desde la API de Star Wars.")
    public List<Films> obtenerFilms(String endpoint) {
        JsonNode filmsData = starWarsApi.fetchStarWarsData(endpoint);
        List<Films> filmsList = new ArrayList<>();

        if (filmsData != null && filmsData.has("results")) {
            List<CompletableFuture<Films>> futures = new ArrayList<>();
            Iterator<JsonNode> elements = filmsData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode filmNode = elements.next();
                CompletableFuture<Films> future = CompletableFuture.supplyAsync(() -> {
                    Films film = new Films();
                    film.setTitle(filmNode.get("title").asText());
                    film.setEpisodeId(filmNode.get("episode_id").asInt());
                    film.setOpeningCrawl(filmNode.get("opening_crawl").asText());
                    String releaseDateString = filmNode.get("release_date").asText();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate releaseDate = LocalDate.parse(releaseDateString, formatter);
                    film.setReleaseDate(releaseDate);
                    film.setDirector(filmNode.get("director").asText());
                    film.setProducer(filmNode.get("producer").asText());
                    film.setUrl(filmNode.get("url").asText());

                    List<People> characters = peopleService.getCharacters(filmNode.get("characters"));
                    film.setCharacters(characters);

                    List<Starships> starships = starshipService.getStarships(filmNode.get("starships"));
                    film.setStarships(starships);

                    List<Vehicles> vehicles = vehiclesService.getVehicles(filmNode.get("vehicles"));
                    film.setVehicles(vehicles);

                    film.setSpecies(getUrlsAsStringList(filmNode.get("species")));

                    return film;
                });
                futures.add(future);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[futures.size()]));

            CompletableFuture<List<Films>> allFilmsFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));

            try {
                filmsList = allFilmsFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return filmsList;
    }
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
     * Obtiene una lista de películas filtradas por URLs de la API de Star Wars.
     *
     * @param filmsNode Nodo JSON que contiene URLs de películas.
     * @return Una lista de objetos Films que coinciden con las URLs proporcionadas.
     */
    @ApiOperation(value = "Obtener películas filtradas por URLs", notes = "Obtiene una lista de películas filtradas por URLs de la API de Star Wars.")

    public List<Films> getFilms(JsonNode filmsNode) {
        List<Films> films = new ArrayList<>();
        if (filmsNode != null && filmsNode.isArray()) {
            List<Films> filmList = obtenerComparatorFilms("films/");
            for (JsonNode filmsUrlNode : filmsNode) {
                String filmsUrl = filmsUrlNode.asText();
                for (Films film : filmList) {
                    if (filmsUrl.equals(film.getUrl())) {
                        films.add(film);
                        break;
                    }
                }
            }
        }
        return films;
    }
    /**
     * Obtiene una lista simplificada de películas para comparación.
     *
     * @param endpoint El endpoint de la API de Star Wars para las películas.
     * @return Una lista de objetos Films simplificados para comparación.
     */
    @ApiOperation(value = "Obtener películas simplificadas para comparación", notes = "Obtiene una lista simplificada de películas desde la API de Star Wars.")

    public List<Films> obtenerComparatorFilms(String endpoint) {
        JsonNode filmsData = starWarsApi.fetchStarWarsData(endpoint);
        List<Films> filmsList = new ArrayList<>();

        if (filmsData != null && filmsData.has("results")) {
            Iterator<JsonNode> elements = filmsData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode filmNode = elements.next();
                Films film = new Films();
                film.setTitle(filmNode.get("title").asText());
                film.setUrl(filmNode.get("url").asText());
                filmsList.add(film);
            }
        }

        return filmsList;
    }


    public List<Films> obtenerFilmsOrdenadosPorId(String endpoint) {
        List<Films> filmsList = obtenerFilms(endpoint);
        return filmsList.stream()
                .sorted(Comparator.comparing(Films::getEpisodeId))
                .collect(Collectors.toList());
    }

    public List<Films> obtenerFilmsOrdenadosPorNombre(String endpoint) {
        List<Films> filmsList = obtenerFilms(endpoint);
        return filmsList.stream()
                .sorted(Comparator.comparing(Films::getTitle))
                .collect(Collectors.toList());
    }


}
