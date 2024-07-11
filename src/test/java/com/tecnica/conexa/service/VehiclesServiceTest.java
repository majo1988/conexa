package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Vehicles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehiclesServiceTest {

    @Mock
    private StarWarsApi starWarsApi;

    @Mock
    private PeopleService peopleService;

    @Mock
    private FilmsService filmsService;

    @InjectMocks
    private VehiclesService vehiclesService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode mockVehiclesData;

    @BeforeEach
    void setUp() throws Exception {
        String jsonString = "{ \"results\": [{ \"name\": \"Sand Crawler\", \"model\": \"Digger Crawler\", \"vehicle_class\": \"wheeled\", \"manufacturer\": \"Corellia Mining Corporation\", \"length\": \"36.8\", \"crew\": \"46\", \"consumables\": \"2 months\", \"max_atmosphering_speed\": \"30\", \"pilots\": [], \"films\": [], \"url\": \"http://swapi.dev/api/vehicles/4/\" }] }";
        mockVehiclesData = objectMapper.readTree(jsonString);
    }

    @Test
    void testObtenerVehicles() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockVehiclesData);
        when(peopleService.getCharacters(any(JsonNode.class))).thenReturn(Collections.emptyList());
        when(filmsService.getFilms(any(JsonNode.class))).thenReturn(Collections.emptyList());

        List<Vehicles> vehiclesList = vehiclesService.obtenerVehicles("vehicles/");
        assertNotNull(vehiclesList);
        assertEquals(1, vehiclesList.size());
        assertEquals("Sand Crawler", vehiclesList.get(0).getName());
        assertEquals("Digger Crawler", vehiclesList.get(0).getModel());
    }

    @Test
    void testGetVehicles() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockVehiclesData);
        List<Vehicles> vehiclesList = vehiclesService.obtenerComparatorVehicles("vehicles/");
        assertNotNull(vehiclesList);
        assertEquals(1, vehiclesList.size());
        assertEquals("Sand Crawler", vehiclesList.get(0).getName());
    }

    @Test
    void testObtenerComparatorVehicles() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockVehiclesData);
        List<Vehicles> vehiclesList = vehiclesService.obtenerComparatorVehicles("vehicles/");
        assertNotNull(vehiclesList);
        assertEquals(1, vehiclesList.size());
        assertEquals("Sand Crawler", vehiclesList.get(0).getName());
    }
}
