package com.tecnica.conexa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Films {

    @Id
    private Integer episodeId;
    private String title;
    private String openingCrawl;
    private LocalDate releaseDate;
    private String director;
    private String producer;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<People> characters;
    @ElementCollection
    private List<String> planets;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Starships> starships;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Vehicles> vehicles;
    @ElementCollection
    private List<String> species;
    private String url;
    private String created;
    private String edited;

    /**
     * Constructor para prueba
     */
    public Films(int i, String film2, String director2, String producer2, String date, String url2) {
    }
}