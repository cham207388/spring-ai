package com.abcham.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SpringAiApplicationTests {

    @MockitoBean
    private VectorStore vectorStore;

    @MockitoBean(name = "webSearchRAGChatClient")
    private ChatClient webSearchRAGChatClient;

    @Test
    void contextLoads() {

    }

}


