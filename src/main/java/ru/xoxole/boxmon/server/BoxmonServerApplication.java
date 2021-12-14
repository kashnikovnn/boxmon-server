package ru.xoxole.boxmon.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BoxmonServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoxmonServerApplication.class, args);
	}

}
