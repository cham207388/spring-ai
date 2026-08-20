package com.abcham.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final ChatClient chatClient;
    private final ChatClient webSearchRAGChatClient;
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource hrSystemTemplate;

    public RagController(@Qualifier("chatMemoryClient") ChatClient chatClient,
                         @Qualifier("webSearchRAGChatClient") ChatClient webSearchRAGChatClient,
                         VectorStore vectorStore) {

        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        this.webSearchRAGChatClient = webSearchRAGChatClient;
    }

    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {

        return ResponseEntity.ok(chatContent(this.chatClient, username, message));
    }

    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {

        return ResponseEntity.ok(chatContent(this.chatClient, username, message));
    }

    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webSearchChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {

        return ResponseEntity.ok(chatContent(this.webSearchRAGChatClient, username, message));
    }



    private String chatContent(ChatClient chatClient, String username, String message) {

        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
                .user(message).call().content();
    }

}
