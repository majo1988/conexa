package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.Films;
import com.tecnica.conexa.service.FilmsService;
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
@Api(tags = "Films API")
public class FilmsController {

    @Autowired
    private FilmsService filmsService;

    @ApiOperation(value = "Obtener lista de películas", response = List.class)
    @GetMapping("/films")
    public ModelAndView mostrarFilms(ModelAndView mav,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(required = false) String filter) {
        String endpoint = "films/";
        List<Films> filmsList = filmsService.obtenerFilms(endpoint);

        if (filter != null && !filter.isEmpty()) {
            filmsList = filmsList.stream()
                    .filter(film -> film.getEpisodeId().toString().contains(filter.toLowerCase()) ||
                            film.getTitle().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int totalPages = calculateTotalPages(filmsList.size(), size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filmsList.size());
        List<Films> filmsPage = filmsList.subList(fromIndex, toIndex);

        mav.addObject("films", filmsPage);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("films-list");
        return mav;
    }

    private int calculateTotalPages(int totalItems, int size) {
        return (int) Math.ceil((double) totalItems / size);
    }
}
