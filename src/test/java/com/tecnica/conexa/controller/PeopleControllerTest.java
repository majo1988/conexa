package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.service.PeopleService;
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

class PeopleControllerTest {

    @Mock
    private PeopleService peopleService;

    @InjectMocks
    private PeopleController peopleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarPeople_debeRetornarListaDePeople() {

        String endpoint = "people/";
        People person1 = new People(1, "Luke Skywalker", "male", "172", "77", "blond", "fair", "blue", "19BBY", "url1");
        People person2 = new People(2, "Leia Organa", "female", "150", "49", "brown", "light", "brown", "19BBY", "url2");
        List<People> peopleList = Arrays.asList(person1, person2);

        when(peopleService.obtenerPeople(endpoint)).thenReturn(peopleList);

        int page = 0;
        int size = 10;
        String filter = "";
        ModelAndView mav = peopleController.mostrarPeople(new ModelAndView(), page,  size, filter );

        assertEquals("people-list", mav.getViewName());
        assertEquals(peopleList, mav.getModel().get("peoples"));
    }
}
