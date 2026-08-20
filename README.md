# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.0/gradle-plugin)
* [chat-memory](https://docs.spring.io/spring-ai/reference/api/chat-memory.html)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.0/gradle-plugin/packaging-oci-image.html)
* [Quadrant](https://qdrant.tech/)
* [Quadrant GitHub](https://github.com/qdrant/qdrant)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

## RAG

- db
  - [Quadrant](https://qdrant.tech/)
  - [Quadrant GitHub](https://github.com/qdrant/qdrant)
- dependency
  - org.springframework.ai:spring-ai-rag
  - org.springframework.ai:spring-ai-starter-vector-store-qdrant
- [dashboard](http://localhost:6333/dashboard)

# random loader

- we load the database with random sentences and utilize prompt template to search the document
- we use advisors and data retrievers
- For simplicity, we configured both advisors and retrievers in the chat client configuration
- There is no need for a SearchRequest object with data retrievers. Hence, making the configuration cleaner