package com.tecnica.conexa.entity;

import lombok.Data;

import javax.persistence.Entity;


@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}
