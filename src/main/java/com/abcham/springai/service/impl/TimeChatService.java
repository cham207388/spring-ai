package com.abcham.springai.service.impl;

import com.abcham.springai.service.IChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
@Qualifier("timeChatService")
public class TimeChatService implements IChatService {

    private final ChatClient chatClient;

    public TimeChatService(@Qualifier("timeChatClient") ChatClient chatClient) {

        this.chatClient = chatClient;
    }

    @Override
    public String answer(String message) {

        return "";
    }

    @Override
    public String answer(String username, String message) {

        return chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
    }

}
