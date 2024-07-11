package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Vehicles;
import com.tecnica.conexa.entity.Vehicles;
import com.tecnica.conexa.service.VehiclesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
@Api(tags = "Vehicles API")
public class VehiclesController {

    @Autowired
    private VehiclesService vehiclesService;

    @ApiOperation(value = "Obtener lista de Naves estelares", response = List.class)
    @GetMapping("/vehicles")
    public ModelAndView mostrarVehicless(ModelAndView mav,    @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String filter) {
        String endpoint = "vehicles/";
        List<Vehicles> vehiclesList = vehiclesService.obtenerVehicles(endpoint);
        if (filter != null && !filter.isEmpty()) {
            vehiclesList = vehiclesList.stream()
                    .filter(starship -> String.valueOf(starship.getId()).contains(filter.toLowerCase()) ||
                            starship.getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int totalPages = calculateTotalPages(vehiclesList.size(), size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, vehiclesList.size());
        List<Vehicles> vehiclesPage = vehiclesList.subList(fromIndex, toIndex);

        mav.addObject("vehicles", vehiclesPage);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("vehicles-list");
        return mav;
    }

    private int calculateTotalPages(int totalItems, int size) {
        return (int) Math.ceil((double) totalItems / size);
    }
}

