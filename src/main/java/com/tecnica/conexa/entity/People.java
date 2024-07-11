package com.tecnica.conexa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String birthYear;
    private String eyeColor;
    private List<Films> films;
    private String gender;
    private String hairColor;
    private String height;
    private String homeworld;
    private String mass;
    private String name;
    private String skinColor;
    private Date created;
    private Date edited;
    private List<String> species;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Starships> starships;
    private String url;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Vehicles> vehicles;
    /**
     * Constructor para prueba
     */
    public People(int i, String lukeSkywalker, String male, String number, String number1, String blond, String fair, String blue, String s, String url1) {
    }
}
