package com.example.query_service.dto;

import java.util.List;

public record SearchResponse(String answer, List<NewsArticle> newsArticles) {
}
