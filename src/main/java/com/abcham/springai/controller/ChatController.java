package com.abcham.springai.controller;

import com.abcham.springai.model.CountryCities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        return chatClient
                .prompt()
//                .advisors(new TokenUsageAuditAdvisor())
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam String message) {

        return chatClient
                .prompt()
                .user(message)
                .stream()
                .content();
    }

    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCities> structuredBean(@RequestParam String message) {

        var response = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(CountryCities.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-list")
    public ResponseEntity<List<String>> structuredList(@RequestParam String message) {

        var response = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ListOutputConverter());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-map")
    public ResponseEntity<Map<String, Object>> structuredMap(@RequestParam String message) {

        var response = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new MapOutputConverter());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat-bean-list")
    public ResponseEntity<List<CountryCities>> structuredBeanList(@RequestParam String message) {

        var response = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<CountryCities>>() {
                });

        return ResponseEntity.ok(response);
    }

}
