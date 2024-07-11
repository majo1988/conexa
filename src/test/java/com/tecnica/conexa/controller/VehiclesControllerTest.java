package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Vehicles;
import com.tecnica.conexa.service.VehiclesService;
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

class VehiclesControllerTest {

    @Mock
    private VehiclesService vehiclesService;

    @InjectMocks
    private VehiclesController vehiclesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mostrarVehicles_debeRetornarListaDeVehicles() {
        String endpoint = "vehicles/";
        Vehicles vehicle1 = new Vehicles(1, "Sand Crawler", "Digger Crawler", "wheeled", "Corellia Mining Corporation", 150000.0f, "46", "30", "200000.0", "50", "2 months", "30", "url1", null, null);
        Vehicles vehicle2 = new Vehicles(2, "T-16 skyhopper", "T-16 skyhopper", "repulsorcraft", "Incom Corporation", 14500.0f, "10.4", "1", "0.5", "1", "0", "0", "url2", null, null);
        List<Vehicles> vehiclesList = Arrays.asList(vehicle1, vehicle2);

        when(vehiclesService.obtenerVehicles(endpoint)).thenReturn(vehiclesList);
        int page = 0;
        int size = 10;
        String filter = "";
        ModelAndView mav = vehiclesController.mostrarVehicless(new ModelAndView(), page,  size, filter );

        assertEquals("vehicles-list", mav.getViewName());
        assertEquals(vehiclesList, mav.getModel().get("vehicles"));
    }
}
