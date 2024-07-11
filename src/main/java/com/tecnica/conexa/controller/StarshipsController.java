package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.entity.Starships;
import com.tecnica.conexa.service.StarshipService;
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
@Api(tags = "Starships API")
public class StarshipsController {

    @Autowired
    private StarshipService starshipsService;

    @ApiOperation(value = "Obtener lista de Naves estelares", response = List.class)
    @GetMapping("/starships")
    public ModelAndView mostrarStarships(ModelAndView mav,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) String filter) {
        String endpoint = "starships/";
        List<Starships> starshipsList = starshipsService.obtenerStarships(endpoint);

        if (filter != null && !filter.isEmpty()) {
            starshipsList = starshipsList.stream()
                    .filter(starship -> String.valueOf(starship.getId()).contains(filter.toLowerCase()) ||
                            starship.getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int totalPages = calculateTotalPages(starshipsList.size(), size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, starshipsList.size());
        List<Starships> starshipsPage = starshipsList.subList(fromIndex, toIndex);

        mav.addObject("starships", starshipsPage);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("starships-list");
        return mav;
    }

    private int calculateTotalPages(int totalItems, int size) {
        return (int) Math.ceil((double) totalItems / size);
    }
}

