package com.github.roveraven.TrainingTelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrainingTelegrambotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainingTelegrambotApplication.class, args);
	}

}
