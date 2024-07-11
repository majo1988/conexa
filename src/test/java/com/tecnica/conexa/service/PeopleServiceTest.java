package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.People;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {

    @Mock
    private StarWarsApi starWarsApi;

    @Mock
    private FilmsService filmsService;

    @Mock
    private VehiclesService vehiclesService;

    @Mock
    private StarshipService starshipService;

    @InjectMocks
    private PeopleService peopleService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode mockPeopleData;

    @BeforeEach
    void setUp() throws Exception {
        // Prepare mock JSON data
        String jsonString = "{ \"results\": [{ \"name\": \"Luke Skywalker\", \"birth_year\": \"19BBY\", \"eye_color\": \"blue\", \"gender\": \"male\", \"hair_color\": \"blond\", \"height\": \"172\", \"mass\": \"77\", \"skin_color\": \"fair\", \"homeworld\": \"http://swapi.dev/api/planets/1/\", \"species\": [], \"url\": \"http://swapi.dev/api/people/1/\", \"films\": [], \"starships\": [], \"vehicles\": [] }] }";
        mockPeopleData = objectMapper.readTree(jsonString);
    }

    @Test
    void testObtenerPeople() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockPeopleData);
        when(filmsService.getFilms(any())).thenReturn(Collections.emptyList());
        when(starshipService.getStarships(any())).thenReturn(Collections.emptyList());
        when(vehiclesService.getVehicles(any())).thenReturn(Collections.emptyList());

        List<People> peopleList = peopleService.obtenerPeople("people/");
        assertNotNull(peopleList);
        assertEquals(1, peopleList.size());
        assertEquals("Luke Skywalker", peopleList.get(0).getName());
        assertEquals("19BBY", peopleList.get(0).getBirthYear());
    }

    @Test
    void testGetCharacters() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockPeopleData);
        List<People> peopleList = peopleService.obtenerComparatorPeople("people/");
        assertNotNull(peopleList);
        assertEquals(1, peopleList.size());
        assertEquals("Luke Skywalker", peopleList.get(0).getName());
    }

    @Test
    void testObtenerComparatorPeople() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockPeopleData);
        List<People> peopleList = peopleService.obtenerComparatorPeople("people/");
        assertNotNull(peopleList);
        assertEquals(1, peopleList.size());
        assertEquals("Luke Skywalker", peopleList.get(0).getName());
    }
}
