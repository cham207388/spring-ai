package com.abcham.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OpenChatClientController {

    private final ChatClient openChatClient;

    public OpenChatClientController(@Qualifier("openChatClient") ChatClient chatClient) {

        this.openChatClient = chatClient;
    }

    @GetMapping("/open-chat")
    public String openChat(@RequestParam("message") String message) {

        return openChatClient.prompt()
                .user(message)
                .call().content();
    }

}
