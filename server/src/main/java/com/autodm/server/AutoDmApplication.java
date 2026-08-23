package com.autodm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AutoDmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoDmApplication.class, args);
	}

}
