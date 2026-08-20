package com.abcham.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource hrSystemTemplate;

    public RagController(@Qualifier("chatMemoryClient") ChatClient chatClient, VectorStore vectorStore) {

        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {

        SearchRequest request = SearchRequest.builder()
                .query(message).topK(4).similarityThreshold(0.5).build();
        List<Document> similarDocs = vectorStore.similaritySearch(request);

        String similarContext = similarDocs.stream()
                .map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        String content = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
                        .param("documents", similarContext))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
                .user(message).call().content();

        return ResponseEntity.ok(content);
    }

    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {

        SearchRequest request = SearchRequest.builder()
                .query(message).topK(4).similarityThreshold(0.5).build();
        List<Document> similarDocs = vectorStore.similaritySearch(request);

        String similarContext = similarDocs.stream()
                .map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        String content = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(hrSystemTemplate)
                        .param("documents", similarContext))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
                .user(message).call().content();

        return ResponseEntity.ok(content);
    }
}
