package com.tecnica.conexa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Starships {

    @Id
    private int id;
    private String name;
    private String model;
    private String starshipClass;
    private String manufacturer;
    private String costInCredits;
    private String length;
    private String crew;
    private String passengers;
    private String cargoCapacity;
    private String consumables;
    private String hyperdriveRating;
    private String mglt;
    private String maxAtmospheringSpeed;
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
    public Starships(int i, String s, String s1, String starfighter, String incomCorporation, String number, String s2, String number1, String number2, String number3, String s3, String s4, String number4, String url1, Object o, Object o1) {
    }
}
