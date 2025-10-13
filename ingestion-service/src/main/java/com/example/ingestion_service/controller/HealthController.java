package com.example.ingestion_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public static String getHealthStatus(){
        return "Ingestion Service is UP";
    }
}
