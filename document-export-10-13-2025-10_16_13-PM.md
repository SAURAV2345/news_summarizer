# News Summarizer

 ✅ **Three Spring Boot microservices** (ingestion, processing, query)
 ✅ **Kafka-based event streaming** between them
 ✅ **Postgres + pgvector** for persistence and semantic search
 ✅ **OpenAI embeddings + augmentation** for intelligent querying
 ✅ **Dockerized and deployed** into **Kubernetes**


Pre-req:

1) data come in the ingestion Service or is generated here dummy and send to Kafka

2) Processing service will do some processing on the article received from Kafka and get the embeddings for the cleaned article from Open AI and push to Kafka.

3) Query service will receive this articles and their embeddings and store it in postgresDB.

Steps:

1) User : what is the latest on AI?

2) Query Service : Convert the question to embeddings using Open AI.

3) Query Service : Query the postgresDB with the embeddings in step 2, to get similar articles based on embeddings.

4) Query service will send this articles as context along with the question to open AI to get a summary.

