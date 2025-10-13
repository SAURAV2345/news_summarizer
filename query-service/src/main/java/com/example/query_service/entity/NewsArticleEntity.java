package com.example.query_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "news_article",schema = "public")
public class NewsArticleEntity {
    @Id
    public String id;
    public String title;
    public String content;

    @Column(columnDefinition = "vector(1536)")
    private float[] embedding;

    public NewsArticleEntity(String id, String title, String content,float[] embedding) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.embedding=embedding;
    }
    public NewsArticleEntity(){}
}
