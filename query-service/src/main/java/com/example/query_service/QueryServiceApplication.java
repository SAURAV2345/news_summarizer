package com.example.query_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class QueryServiceApplication {

	public static void main(String[] args) {
		System.out.println(">>> JVM TimeZone: " + TimeZone.getDefault().getID());
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(QueryServiceApplication.class, args);
	}

}
