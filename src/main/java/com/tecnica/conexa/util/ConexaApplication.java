package com.tecnica.conexa.util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConexaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConexaApplication.class, args);
	}
}
