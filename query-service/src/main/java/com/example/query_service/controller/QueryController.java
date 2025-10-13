package com.example.query_service.controller;


import com.example.query_service.dto.NewsArticle;
import com.example.query_service.dto.SearchResponse;
import com.example.query_service.repository.ArticleRepository;
import com.example.query_service.service.QueryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class QueryController {
    private List<NewsArticle> articles = new ArrayList<NewsArticle>();
    private final QueryService queryService;


    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping("/storeRest")
    public String storeRest(@RequestBody NewsArticle newsArticle){
        articles.add(newsArticle);
        return "Stored article: "+newsArticle.id;
    }

   @KafkaListener(topics = "processed-articles",groupId = "query-service")
    public String storeMemory(@RequestBody NewsArticle newsArticle){
        articles.add(newsArticle);
        // adding persistence to Postgres DB and moving the logic to a service class
       queryService.save(newsArticle);
        return "Stored article: "+newsArticle.id;
    }

    @GetMapping("/articles")
    public List<NewsArticle> getArticles(@RequestParam(value = "offset",defaultValue = "0") int offset,
                                         @RequestParam(value = "limit",defaultValue = "10") int limit){
        //In-memory solution
        List<NewsArticle> articleList =articles.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        // DB solution
        Pageable pageable = PageRequest.of(offset,limit);
        return queryService.findAll(pageable);
    }
    @GetMapping("/search")
    public List<NewsArticle> search(@RequestParam(value = "query",defaultValue = "") String query){
        return queryService.search(query);

    }

    @GetMapping("/search_LLM")
    public SearchResponse searchAndAnswer(@RequestParam(value = "query",defaultValue = "") String query){
        return queryService.searchAndAnswer(query);

    }
}
