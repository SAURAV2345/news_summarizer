package com.example.query_service.repository;


import com.example.query_service.dto.NewsArticle;
import com.example.query_service.entity.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<NewsArticleEntity,String> {
    @Query(value = """
            Select id,title,content
            From news_article
            Order BY embedding <-> CAST(:searchEmbeddings AS vector)
            LIMIT :k""",nativeQuery = true)
    List<NewsArticle> findTopKSimilar(@Param("searchEmbeddings")String searchEmbeddings,@Param("k") int k);
}
