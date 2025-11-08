# 📰 Real-Time News Summarizer & Vector Search (Spring Boot + Kafka + pgvector + Kubernetes)

A **production-style distributed system** built with microservices, streaming, vector search, and Kubernetes autoscaling — designed to simulate how modern companies build real-time data pipelines + RAG (Retrieval Augmented Generation).

This project takes live (or dummy) news articles ➝ processes them asynchronously ➝ stores embeddings ➝ enables semantic search.

---

## 🏗️ Architecture

```mermaid
flowchart LR
    A[Ingestion Service] -->|Raw News JSON| B((Kafka Topic: raw-news))
    B --> C[Processing Service]
    C -->|Summary + Embedding| D((Kafka Topic: processed-news))
    D --> E[Query Service]
    E -->|PGVector Similarity| F[(Postgres + pgvector)]
