package com.example.ingestion_service.controller;

import com.example.ingestion_service.dto.NewsArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class IngestionController {
    @Autowired
    private KafkaTemplate<String,NewsArticle> kafkaTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/ingestRest")
    public String ingestRest(@RequestBody NewsArticle newsArticle){
        String response = restTemplate.postForObject("http://localhost:8082/process",newsArticle,String.class);
        return "Ingestion OK, Processing says"+ response;
    }

    @PostMapping("/ingest")
    public String ingest(@RequestBody NewsArticle newsArticle){
       kafkaTemplate.send("raw-articles",newsArticle.id,newsArticle);
       return "Ingested to kafka topic: raw-articles";
    }

    @GetMapping("/load")
    public String generateLoad(@RequestParam(value = "count",defaultValue = "100") int count){
        for(int i=1;i<=count;i++){
            NewsArticle na =new NewsArticle();
            na.setId(UUID.randomUUID().toString());
            na.setTitle("Title"+i);
            na.setContent("Artificial Intelligence is transforming healthcare with better diagnostics and personalized treatment." + i + "....");
            kafkaTemplate.send("raw-articles",na.id,na);

        }
        return "Published " + count + " articles!";
    }
}
