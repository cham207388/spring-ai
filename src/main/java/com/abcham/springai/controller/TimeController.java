package com.abcham.springai.controller;

import com.abcham.springai.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tools")
public class TimeController {

    private final IChatService chatService;

    public TimeController(@Qualifier("timeChatService") IChatService chatService) {

        this.chatService = chatService;
    }


    @GetMapping("/local-time")
    public ResponseEntity<String> localTime(@RequestHeader("username") String username,
                                            @RequestParam("message") String message) {

        return ResponseEntity.ok(chatService.answer(username, message));
    }

}
