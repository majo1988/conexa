package com.tecnica.conexa.util;

import com.tecnica.conexa.entity.User;
import com.tecnica.conexa.repository.UsuarioRepository;
import com.tecnica.conexa.service.UsuarioService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@Order(1)
public class UserDefault implements CommandLineRunner {


    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioService usuarioService;


    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("creando usuario para el login");

                User userDefault = new User("conexa", "carmelita23" );

                usuarioService.create(userDefault);

        } catch (Exception e) {
                log.error("Error al guardar usuario");

        }
    }
}
