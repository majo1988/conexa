package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.service.FilmsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class FilmsControllerTest {

    @Mock
    private FilmsService filmsService;

    @InjectMocks
    private FilmsController filmsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarFilms_debeRetornarListaDeFilms() {
        String endpoint = "films/";
        int page = 0;
        int size = 10;
        String filter = "";
        Films film1 = new Films(1, "Film1", "Director1", "Producer1", "2024-01-01", "url1");
        Films film2 = new Films(2, "Film2", "Director2", "Producer2", "2024-01-02", "url2");
        List<Films> filmsList = Arrays.asList(film1, film2);

        when(filmsService.obtenerFilms(endpoint)).thenReturn(filmsList);


        ModelAndView mav = filmsController.mostrarFilms(new ModelAndView(), page, size, filter);

        assertEquals("films-list", mav.getViewName());
        assertEquals(filmsList, mav.getModel().get("films"));
    }
}
