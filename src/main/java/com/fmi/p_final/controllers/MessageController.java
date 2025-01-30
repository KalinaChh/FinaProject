package com.fmi.p_final.controllers;

import com.fmi.p_final.entities.Message;
import com.fmi.p_final.entities.MockedSecurity;
import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/user/{userId}/sent")
    public ResponseEntity<AppResponse<List<Message>>> getMessagesSentByUser(@PathVariable Long userId) {
        return messageService.getMessagesSentByUser(userId);
    }

    @GetMapping("/user/{userId}/received")
    public ResponseEntity<AppResponse<List<Message>>> getMessagesReceivedByUser(@PathVariable Long userId) {
        return messageService.getMessagesReceivedByUser(userId);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<AppResponse<List<Message>>> getMessagesInChannel(@PathVariable Long channelId) {
        return messageService.getMessagesInChannel(channelId);
    }

    @PostMapping("/send")
    public ResponseEntity<AppResponse<Message>> sendDirectMessage(
            @RequestParam Long senderId,
            @RequestParam Long recipientId,
            @RequestParam String content) {
        return messageService.sendDirectMessage(senderId, recipientId, content);
    }

    @PostMapping("/send/channel")
    public ResponseEntity<AppResponse<Message>> sendMessageToChannel(
            @Valid @RequestBody Message message) {
        return messageService.sendMessageToChannel(message);
    }


    @DeleteMapping("/{messageId}")
    public ResponseEntity<AppResponse<Object>> deleteMessage(
            @PathVariable Long messageId,
            @RequestHeader(value = "Mocked-Token") String mockedToken,
            @RequestHeader(value = "Mocked-RequesterId") Long mockedRequesterId) {

        MockedSecurity ms = new MockedSecurity();
        ms.token = mockedToken;
        ms.requesterId = mockedRequesterId;

        return messageService.deleteMessage(messageId, ms);
    }

}
