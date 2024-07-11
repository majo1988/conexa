package com.tecnica.conexa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnica.conexa.api.StarWarsApi;
import com.tecnica.conexa.entity.Starships;
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
public class StarshipsServiceTest {

    @Mock
    private StarWarsApi starWarsApi;

    @Mock
    private PeopleService peopleService;

    @Mock
    private FilmsService filmsService;

    @InjectMocks
    private StarshipService starshipService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode mockStarshipsData;

    @BeforeEach
    void setUp() throws Exception {
        // Preparar datos JSON simulados
        String jsonString = "{ \"results\": [{ \"name\": \"Millennium Falcon\", \"model\": \"YT-1300 light freighter\", \"starship_class\": \"Light freighter\", \"manufacturer\": \"Corellian Engineering Corporation\", \"cost_in_credits\": \"100000\", \"length\": \"34.37\", \"crew\": \"4\", \"passengers\": \"6\", \"cargo_capacity\": \"100000\", \"consumables\": \"2 months\", \"hyperdrive_rating\": \"0.5\", \"MGLT\": \"75\", \"max_atmosphering_speed\": \"1050\", \"pilots\": [], \"films\": [], \"url\": \"http://swapi.dev/api/starships/10/\" }] }";
        mockStarshipsData = objectMapper.readTree(jsonString);
    }

    @Test
    void testObtenerStarships() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockStarshipsData);
        when(peopleService.getCharacters(any(JsonNode.class))).thenReturn(Collections.emptyList());
        when(filmsService.getFilms(any(JsonNode.class))).thenReturn(Collections.emptyList());

        List<Starships> starshipsList = starshipService.obtenerStarships("starships/");
        assertNotNull(starshipsList);
        assertEquals(1, starshipsList.size());
        assertEquals("Millennium Falcon", starshipsList.get(0).getName());
        assertEquals("YT-1300 light freighter", starshipsList.get(0).getModel());
    }

    @Test
    void testGetStarships() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockStarshipsData);
        List<Starships> starshipsList = starshipService.obtenerComparatorStarships("starships/");
        assertNotNull(starshipsList);
        assertEquals(1, starshipsList.size());
        assertEquals("Millennium Falcon", starshipsList.get(0).getName());
    }

    @Test
    void testObtenerComparatorStarships() {
        when(starWarsApi.fetchStarWarsData(anyString())).thenReturn(mockStarshipsData);
        List<Starships> starshipsList = starshipService.obtenerComparatorStarships("starships/");
        assertNotNull(starshipsList);
        assertEquals(1, starshipsList.size());
        assertEquals("Millennium Falcon", starshipsList.get(0).getName());
    }
}
