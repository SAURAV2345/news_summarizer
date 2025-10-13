package com.example.query_service.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {
    @Value("${OPENAI_API_KEY}")
    private String apiKey;
    @Bean
    public OpenAiService openAiService(){
        return new OpenAiService(apiKey);
    }
}
