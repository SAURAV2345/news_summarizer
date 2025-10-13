package com.example.query_service.dto;

public class NewsArticle {
    public String id;
    public String title;
    public String content;

    private float[] embedding;

    public NewsArticle(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public NewsArticle() {

    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
