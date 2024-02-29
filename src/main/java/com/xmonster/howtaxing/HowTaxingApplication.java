package com.xmonster.howtaxing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
public class HowTaxingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HowTaxingApplication.class, args);
	}
}