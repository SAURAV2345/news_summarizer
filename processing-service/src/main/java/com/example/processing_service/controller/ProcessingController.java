package com.example.processing_service.controller;

import com.example.processing_service.dto.NewsArticle;
import com.example.processing_service.service.ProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProcessingController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ProcessingService processingService;
    @Autowired
    private KafkaTemplate<String,NewsArticle> kafkaTemplate;

    public ProcessingController(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @PostMapping("/processRest")
    public String processRest(@RequestBody NewsArticle newsArticle){
        // Fake summarization
        newsArticle.setContent(newsArticle.getContent()+"_processed");
        String response = restTemplate.postForObject("http://localhost:8083/store",newsArticle,String.class);
        return "Processed Article "+newsArticle.id;
    }
    @KafkaListener(topics = "raw-articles",groupId = "processing-service")
    @Transactional("KafkaTransactionalManager")
    public void process(@RequestBody NewsArticle newsArticle){
        NewsArticle enrichedData = processingService.process(newsArticle);
       kafkaTemplate.send("processed-articles",enrichedData.id,enrichedData);
    }
}
