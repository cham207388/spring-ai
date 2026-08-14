package com.abcham.springai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient openAiChatClient;
    private final ChatClient bedrockChatClient;

    public ChatController(
            @Qualifier("openAiChatClient") ChatClient openAiChatClient,
            @Qualifier("bedrockChatClient") ChatClient bedrockChatClient) {
        this.openAiChatClient = openAiChatClient;
        this.bedrockChatClient = bedrockChatClient;
    }

    @GetMapping("/openai/chat")
    public String chatOpenAi(@RequestParam String message) {
        log.info("OpenAI chat message: {}", message);
        String content = openAiChatClient.prompt(message).call().content();
        log.info("OpenAI response: {}", content);
        return content;
    }

    @GetMapping("/bedrock/chat")
    public String chatBedrock(@RequestParam String message) {
        log.info("Bedrock chat message: {}", message);
        String content = bedrockChatClient.prompt(message).call().content();
        log.info("Bedrock response: {}", content);
        return content;
    }
}
