package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.entity.Vehicles;
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
@Api(tags = "People Service")
public class PeopleService  {


    @Autowired
    private StarWarsApi starWarsApi;

    @Autowired
    private StarshipService starshipService;

    @Autowired
    private FilmsService filmsService;

    @Autowired
    private VehiclesService vehiclesService;

    private int currentId = 1;

    /**
     * Obtiene una lista de personas desde la API de Star Wars.
     *
     * @param endpoint El endpoint de la API de Star Wars para las personas.
     * @return Una lista de objetos People que representan las personas obtenidas.
     */
    @ApiOperation(value = "Obtener personas de Star Wars", notes = "Obtiene una lista de personas desde la API de Star Wars.")
    public List<People> obtenerPeople(String endpoint) {
        JsonNode peopleData = starWarsApi.fetchStarWarsData(endpoint);
        List<People> peopleList = new ArrayList<>();

        if (peopleData != null && peopleData.has("results")) {
            List<CompletableFuture<People>> futures = new ArrayList<>();
            Iterator<JsonNode> elements = peopleData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode personNode = elements.next();
                CompletableFuture<People> future = CompletableFuture.supplyAsync(() -> {
                    People person = new People();
                    person.setId(currentId++);
                    person.setName(personNode.get("name").asText());
                    person.setBirthYear(personNode.get("birth_year").asText());
                    person.setEyeColor(personNode.get("eye_color").asText());
                    person.setGender(personNode.get("gender").asText());
                    person.setHairColor(personNode.get("hair_color").asText());
                    person.setHeight(personNode.get("height").asText());
                    person.setMass(personNode.get("mass").asText());
                    person.setSkinColor(personNode.get("skin_color").asText());
                    person.setHomeworld(personNode.get("homeworld").asText());
                    person.setSpecies(getUrlsAsStringList(personNode.get("species")));
                    person.setUrl(personNode.get("url").asText());

                    List<Films> films = filmsService.getFilms(personNode.get("films"));
                    person.setFilms(films);

                    List<Starships> starships = starshipService.getStarships(personNode.get("starships"));
                    person.setStarships(starships);

                    List<Vehicles> vehicles = vehiclesService.getVehicles(personNode.get("vehicles"));
                    person.setVehicles(vehicles);

                    return person;
                });
                futures.add(future);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[futures.size()]));

            CompletableFuture<List<People>> allPeopleFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));

            try {
                peopleList = allPeopleFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return peopleList;
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
     * Obtiene una lista de personas filtradas por URLs de la API de Star Wars.
     *
     * @param charactersNode Nodo JSON que contiene URLs de personas.
     * @return Una lista de objetos People que coinciden con las URLs proporcionadas.
     */
    @ApiOperation(value = "Obtener personas filtradas por URLs", notes = "Obtiene una lista de personas filtradas por URLs de la API de Star Wars.")
    public List<People> getCharacters(JsonNode charactersNode) {
        List<People> characters = new ArrayList<>();
        if (charactersNode != null && charactersNode.isArray()) {
            List<People> peopleList = obtenerComparatorPeople("people/");
            for (JsonNode characterUrlNode : charactersNode) {
                String characterUrl = characterUrlNode.asText();
                for (People person : peopleList) {
                    if (characterUrl.equals(person.getUrl())) {
                        characters.add(person);
                        break;
                    }
                }
            }
        }
        return characters;
    }
    /**
     * Obtiene una lista simplificada de personas para comparación.
     *
     * @param endpoint El endpoint de la API de Star Wars para las personas.
     * @return Una lista de objetos People simplificados para comparación.
     */
    @ApiOperation(value = "Obtener personas simplificadas para comparación", notes = "Obtiene una lista simplificada de personas desde la API de Star Wars.")
    public List<People> obtenerComparatorPeople(String endpoint) {

        JsonNode peopleData = starWarsApi.fetchStarWarsData(endpoint);
        List<People> peopleList = new ArrayList<>();

        if (peopleData != null && peopleData.has("results")) {
            Iterator<JsonNode> elements = peopleData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode personNode = elements.next();
                People person = new People();
                person.setId(currentId++);
                person.setName(personNode.get("name").asText());
                person.setUrl(personNode.get("url").asText());
                peopleList.add(person);
            }
        }

        return peopleList;
    }

}
