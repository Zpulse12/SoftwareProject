package com.example.klantenportaal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KlantenportaalApplication {

	public static void main(String[] args) {
		SpringApplication.run(KlantenportaalApplication.class, args);
	}
}
