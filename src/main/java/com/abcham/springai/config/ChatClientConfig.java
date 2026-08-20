package com.abcham.springai.config;

import com.abcham.springai.advisor.TokenUsageAuditAdvisor;
import com.abcham.springai.rag.PIIMaskingDocumentPostProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {

        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .build();
    }

    @Bean("chatMemoryClient")
    public ChatClient chatMemoryClient(ChatClient.Builder chatClientBuilder,
                                       ChatMemory chatMemory,
                                       RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {

        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenAdvisor = new TokenUsageAuditAdvisor();
        return chatClientBuilder
                .defaultAdvisors(List.of(memoryAdvisor, loggerAdvisor, retrievalAugmentationAdvisor, tokenAdvisor))
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                              ChatClient.Builder chatClientBuilder) {

        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english").build())
                .documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                        .topK(3).similarityThreshold(0.5).build())
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
                .build();
    }

}
