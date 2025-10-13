package com.example.query_service.service;

import com.example.query_service.dto.NewsArticle;
import com.example.query_service.dto.SearchResponse;
import com.example.query_service.entity.NewsArticleEntity;
import com.example.query_service.repository.ArticleRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class QueryService {
    private final ArticleRepository articleRepository;
    private final OpenAiService openAiService;

    public QueryService(ArticleRepository articleRepository, OpenAiService openAiService) {
        this.articleRepository = articleRepository;
        this.openAiService=openAiService;
    }
    @Transactional
    public void save(NewsArticle newsArticle){
        // convert DTO to Entity object to store in DB
        NewsArticleEntity newsArticleEntity= new NewsArticleEntity(newsArticle.id,
                newsArticle.title,newsArticle.content, newsArticle.getEmbedding());

        articleRepository.save(newsArticleEntity);
    }

    public List<NewsArticle> findAll(Pageable pageable){
        // Need to convert the entity back to the DTO format after fetching from DB
        return articleRepository.findAll(pageable).getContent().stream()
                .map(e->{
                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.id = e.id;
                    newsArticle.title=e.title;
                    newsArticle.content= e.content;
                    return newsArticle;
                }).collect(Collectors.toList());
    }

    public List<NewsArticle> search(String query) {
        float [] searchEmbeddings = generateEmbeddings(query);
        // converting the embeddings to string which is cast to vector so that we can compare properly
        List<NewsArticle> rows = articleRepository.findTopKSimilar(toPgVector(searchEmbeddings),5);
        return rows.stream()
                .map(r -> new NewsArticle(r.id,r.title,r.content))
                .toList();
    }

    private String toPgVector(float[] embedding) {
        List<Float> list = new ArrayList<>(embedding.length);
        for (float f : embedding) list.add(f);
        return "[" + list.stream()
                .map(e->e.toString())
                .collect(Collectors.joining(","))+"]";

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

    public SearchResponse searchAndAnswer(String query) {
        // Step 1: Embed query + find similar articles
        float[] queryEmbedding = generateEmbeddings(query);
        List<NewsArticle> topArticles = articleRepository.findTopKSimilar(toPgVector(queryEmbedding),2);

        // Step 2: Build context
        String context = topArticles.stream()
                .map(a -> a.title + " - " + a.content)
                .reduce("", (acc, s) -> acc + "\n" + s);

        // Step 3: Ask LLM for final answer
        String answer = generateAnswer(query, context);

        return new SearchResponse(answer,topArticles);
    }

    public String generateAnswer(String query, String context) {
        String prompt = """
            You are a helpful assistant. 
            Use the following news context to answer the question.

            Context:
            %s

            Question: %s
            Answer in 3-4 sentences.
            """.formatted(context, query);

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")   // or gpt-3.5-turbo if cheaper
                .messages(List.of(
                        new ChatMessage("system", "You are a news assistant."),
                        new ChatMessage("user", prompt)
                ))
                .maxTokens(300)
                .build();

        return openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();
    }
}

