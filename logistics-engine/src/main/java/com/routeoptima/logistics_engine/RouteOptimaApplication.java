package com.routeoptima.logistics_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RouteOptimaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RouteOptimaApplication.class, args);
	}

}
