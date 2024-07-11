package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.service.StarshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StarshipsControllerTest {

    @Mock
    private StarshipService starshipService;

    @InjectMocks
    private StarshipsController starshipsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarStarships_debeRetornarListaDeStarships() {

        String endpoint = "starships/";
        Starships starship1 = new Starships(1, "X-wing", "T-65 X-wing", "Starfighter", "Incom Corporation", "149999", "12.5", "1", "0", "110", "1 week", "1.0", "100", "url1", null, null);
        Starships starship2 = new Starships(2, "TIE Advanced x1", "Twin Ion Engine Advanced x1", "Starfighter", "Sienar Fleet Systems", "unknown", "9.2", "1", "0", "150", "5 days", "1.0", "105", "url2", null, null);
        List<Starships> starshipsList = Arrays.asList(starship1, starship2);

        int page = 0;
        int size = 10;
        String filter = "";
        when(starshipService.obtenerStarships(endpoint)).thenReturn(starshipsList);

        ModelAndView mav = starshipsController.mostrarStarships(new ModelAndView(), page,  size, filter);

        assertEquals("starships-list", mav.getViewName());
        assertEquals(starshipsList, mav.getModel().get("starships"));
    }
}
