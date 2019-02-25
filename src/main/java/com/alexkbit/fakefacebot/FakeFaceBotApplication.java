package com.alexkbit.fakefacebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableMongoRepositories
public class FakeFaceBotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(FakeFaceBotApplication.class, args);
	}

}
