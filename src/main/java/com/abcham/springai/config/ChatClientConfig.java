package com.abcham.springai.config;

import com.abcham.springai.advisor.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        OpenAiChatOptions.Builder options = OpenAiChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.8)
                .maxCompletionTokens(100);

        return chatClientBuilder
                .defaultOptions(options)
//                .defaultSystem(Constants.DEFAULT_SYSTEM_MSG)
                .defaultAdvisors(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())
                .build();
    }

}
