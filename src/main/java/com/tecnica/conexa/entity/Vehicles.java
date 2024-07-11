package com.tecnica.conexa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicles {

    @Id
    private int id;
    private String name;
    private String model;
    private String vehicleClass;
    private String manufacturer;
    private String costInCredits;
    private float length;
    private String crew;
    private int passengers;
    private int cargoCapacity;
    private String consumables;
    private int maxAtmospheringSpeed;
    private Date created;
    private Date edited;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Films> films;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<People> pilots;
    private String url;

    /**
     * Constructor para prueba
     */
    public Vehicles(int i, String sandCrawler, String diggerCrawler, String wheeled, String corelliaMiningCorporation, float v, String number, String number1, String s, String number2, String s1, String number3, String url1, Object o, Object o1) {
    }
}
