package com.tecnica.conexa.controller;

import com.tecnica.conexa.entity.People;
import com.tecnica.conexa.service.PeopleService;
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
@Api(tags = "People API")
public class PeopleController {

    @Autowired
    private PeopleService peopleService;
    @ApiOperation(value = "Obtener lista de personas", response = List.class)
    @GetMapping("/people")
    public ModelAndView mostrarPeople(ModelAndView mav,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String filter) {
        String endpoint = "people/";
        List<People> peopleList = peopleService.obtenerPeople(endpoint);


        if (filter != null && !filter.isEmpty()) {
            peopleList = peopleList.stream()
                    .filter(people -> String.valueOf(people.getId()).contains(filter.toLowerCase()) ||
                            people.getName().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        int totalPages = calculateTotalPages(peopleList.size(), size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, peopleList.size());
        List<People> peoplePage = peopleList.subList(fromIndex, toIndex);

        mav.addObject("people", peoplePage);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("people-list");
        return mav;
    }


    private int calculateTotalPages(int totalItems, int size) {
            return (int) Math.ceil((double) totalItems / size);
        }
    }
