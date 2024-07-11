package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.entity.Vehicles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmsServiceTest {

    @Mock
    private StarWarsApi starWarsApi;

    @Mock
    private PeopleService peopleService;

    @Mock
    private VehiclesService vehiclesService;

    @Mock
    private StarshipService starshipService;

    @InjectMocks
    private FilmsService filmsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode mockFilmsData;

    @BeforeEach
    void setUp() throws Exception {
        // Prepare mock JSON data
        String jsonString = "{ \"results\": [{ \"title\": \"A New Hope\", \"episode_id\": 4, \"opening_crawl\": \"It is a period of civil war...\", \"release_date\": \"1977-05-25\", \"director\": \"George Lucas\", \"producer\": \"Gary Kurtz, Rick McCallum\", \"url\": \"http://swapi.dev/api/films/1/\", \"characters\": [], \"starships\": [], \"vehicles\": [], \"species\": [] }] }";
        mockFilmsData = objectMapper.readTree(jsonString);
    }

    @Test
    void testObtenerFilms() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockFilmsData);
        when(peopleService.getCharacters(any())).thenReturn(Collections.emptyList());
        when(starshipService.getStarships(any())).thenReturn(Collections.emptyList());
        when(vehiclesService.getVehicles(any())).thenReturn(Collections.emptyList());

        List<Films> filmsList = filmsService.obtenerFilms("films/");
        assertNotNull(filmsList);
        assertEquals(1, filmsList.size());
        assertEquals("A New Hope", filmsList.get(0).getTitle());
        assertEquals(4, filmsList.get(0).getEpisodeId());
    }

    @Test
    void testGetFilms() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockFilmsData);
        List<Films> filmsList = filmsService.obtenerComparatorFilms("films/");
        assertNotNull(filmsList);
        assertEquals(1, filmsList.size());
        assertEquals("A New Hope", filmsList.get(0).getTitle());
    }

    @Test
    void testObtenerComparatorFilms() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockFilmsData);
        List<Films> filmsList = filmsService.obtenerComparatorFilms("films/");
        assertNotNull(filmsList);
        assertEquals(1, filmsList.size());
        assertEquals("A New Hope", filmsList.get(0).getTitle());
    }
}
