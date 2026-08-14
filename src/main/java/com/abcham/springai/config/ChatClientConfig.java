package com.abcham.springai.config;

import com.abcham.springai.utils.Constants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultSystem(Constants.DEFAULT_SYSTEM_MSG)
                .build();
    }
}
