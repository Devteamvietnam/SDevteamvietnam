package com.devteamvietnam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class DevteamvietnamApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevteamvietnamApplication.class, args);
	}

}
