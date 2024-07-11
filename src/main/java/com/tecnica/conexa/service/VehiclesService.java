package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.entity.People;
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
@Api(tags = "Vehicles Service")
public class VehiclesService  {

    @Autowired
    private StarWarsApi starWarsApi;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private FilmsService filmsService;

    private int currentId = 1;

    /**
     * Obtiene una lista de vehicles desde la API de Star Wars.
     *
     * @param endpoint El endpoint de la API de Star Wars para los vehicles.
     * @return Una lista de objetos Vehicles que representan los vehicles obtenidos.
     */
    @ApiOperation(value = "Obtener vehicles de Star Wars", notes = "Obtiene una lista de vehicles desde la API de Star Wars.")
    public List<Vehicles> obtenerVehicles(String endpoint) {
        JsonNode vehiclesData = starWarsApi.fetchStarWarsData(endpoint);
        List<Vehicles> vehiclesList = new ArrayList<>();

        if (vehiclesData != null && vehiclesData.has("results")) {
            List<CompletableFuture<Vehicles>> futures = new ArrayList<>();
            Iterator<JsonNode> elements = vehiclesData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode vehicleNode = elements.next();
                CompletableFuture<Vehicles> future = CompletableFuture.supplyAsync(() -> {
                    Vehicles vehicle = new Vehicles();
                    vehicle.setId(currentId++);
                    vehicle.setName(vehicleNode.get("name").asText());
                    vehicle.setModel(vehicleNode.get("model").asText());
                    vehicle.setVehicleClass(vehicleNode.get("vehicle_class").asText());
                    vehicle.setManufacturer(vehicleNode.get("manufacturer").asText());
                    vehicle.setLength(Float.parseFloat(vehicleNode.get("length").asText()));
                    vehicle.setCrew(vehicleNode.get("crew").asText());
                    vehicle.setConsumables(vehicleNode.get("consumables").asText());
                    vehicle.setMaxAtmospheringSpeed(Integer.parseInt(vehicleNode.get("max_atmosphering_speed").asText()));
                    List<People> pilots = peopleService.getCharacters(vehicleNode.get("pilots"));
                    vehicle.setPilots(pilots);
                    List<Films> films = filmsService.getFilms(vehicleNode.get("films"));
                    vehicle.setFilms(films);
                    vehicle.setUrl(vehicleNode.get("url").asText());
                    return vehicle;
                });
                futures.add(future);
            }

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[futures.size()]));

            CompletableFuture<List<Vehicles>> allVehiclesFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList()));

            try {
                vehiclesList = allVehiclesFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return vehiclesList;
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
     * Obtiene una lista de vehicles filtrados por URLs de la API de Star Wars.
     *
     * @param charactersNode Nodo JSON que contiene URLs de vehicles.
     * @return Una lista de objetos Vehicles que coinciden con las URLs proporcionadas.
     */
    @ApiOperation(value = "Obtener vehicles filtrados por URLs", notes = "Obtiene una lista de vehicles filtrados por URLs de la API de Star Wars.")

    public List<Vehicles> getVehicles(JsonNode charactersNode) {
        List<Vehicles> characters = new ArrayList<>();
        if (charactersNode != null && charactersNode.isArray()) {
            List<Vehicles> peopleList = obtenerComparatorVehicles("vehicles/");
            for (JsonNode characterUrlNode : charactersNode) {
                String characterUrl = characterUrlNode.asText();
                for (Vehicles person : peopleList) {
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
     * Obtiene una lista simplificada de vehicles para comparación.
     *
     * @param endpoint El endpoint de la API de Star Wars para los vehicles.
     * @return Una lista de objetos Vehicles simplificados para comparación.
     */
    @ApiOperation(value = "Obtener vehicles simplificados para comparación", notes = "Obtiene una lista simplificada de vehicles desde la API de Star Wars.")

    public List<Vehicles> obtenerComparatorVehicles(String endpoint) {

        JsonNode vehiclesData = starWarsApi.fetchStarWarsData(endpoint);
        List<Vehicles> vehiclesList = new ArrayList<>();

        if (vehiclesData != null && vehiclesData.has("results")) {
            Iterator<JsonNode> elements = vehiclesData.get("results").elements();

            while (elements.hasNext()) {
                JsonNode vehicleNode = elements.next();
                Vehicles vehicle = new Vehicles();
                vehicle.setId(currentId++);
                vehicle.setName(vehicleNode.get("name").asText());
                vehicle.setUrl(vehicleNode.get("url").asText());
                vehiclesList.add(vehicle);
            }
        }

        return vehiclesList;
    }
}
