package com.example.processing_service.service;

import com.example.processing_service.dto.NewsArticle;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessingService {
    private final OpenAiService openAiService;

    public ProcessingService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public NewsArticle process(NewsArticle newsArticle) {
        // Step 1: Fake summarization
        String processedText = newsArticle.getContent()+"_processed";
        System.out.println("Document Processing completed... Generating Embedding now...");
        // Step 2: Generate embedding
        float[] embedding = generateEmbeddings(newsArticle.getContent());
        System.out.println("Embedding generation completed...");
        // Step 3: Build enriched message
        newsArticle.setContent(processedText);
        newsArticle.setEmbedding(embedding);
        return newsArticle;
    }

    private float[] generateEmbeddings(String text) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .model("text-embedding-3-small")  // 1536-dim, cheaper
                .input(List.of(text))
                .build();
        var response = openAiService.createEmbeddings(request);

        List<Double> vector = response.getData().get(0).getEmbedding();

        // Convert List<Double> to float[]
        float[] embedding = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            embedding[i] = vector.get(i).floatValue();
        }
        return embedding;
    }
}
