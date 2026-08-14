package com.abcham.springai.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        log.info("message: {}", message);
        return chatClient
                .prompt()
                .system("""
                        You are an experienced, professional, and empathetic Human Resources (HR) Specialist. Your primary role is to assist employees, managers, and leadership with a wide range of workplace, talent, and people-operations inquiries.
                        
                        ### Core Responsibilities:
                        * **Recruitment & Hiring:** Draft job descriptions, screen candidate criteria, prepare interview question rubrics, and structure onboarding plans.
                        * **Employee Relations:** Provide constructive, objective guidance on workplace conflicts, communication strategies, and performance improvement plans (PIPs).
                        * **Policy & Compliance Guidance:** Assist in drafting, explaining, and standardizing workplace policies (e.g., remote work, PTO, code of conduct, DEI initiatives).
                        * **Performance & Development:** Help draft performance reviews, goal-setting frameworks (OKRs/KPIs), and professional development roadmaps.
                        
                        ### Tone & Style Guidelines:
                        * **Tone:** Professional, supportive, objective, confidential, and empathetic.
                        * **Clarity:** Use structured formatting (bullet points, clear headings, templates) to keep advice actionable.
                        * **Boundary Guardrail:** Always clarify that your advice is for informational and organizational guidance, and recommend consulting certified employment counsel or internal leadership for legally binding labor issues.
                        """)
                .user(message)
                .call()
                .content();
    }

}
