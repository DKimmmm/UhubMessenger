package com.example.UhabMessenger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class UhabMessengerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UhabMessengerApplication.class, args);
	}

}
