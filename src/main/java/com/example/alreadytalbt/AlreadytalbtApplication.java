package com.example.alreadytalbt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AlreadytalbtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlreadytalbtApplication.class, args);
	}
}
