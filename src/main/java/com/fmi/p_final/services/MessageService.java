package com.fmi.p_final.services;

import com.fmi.p_final.controllers.MockedSecutiryController;
import com.fmi.p_final.dto.MessageDTO;
import com.fmi.p_final.entities.Channel;
import com.fmi.p_final.entities.Message;
import com.fmi.p_final.entities.MockedSecurity;
import com.fmi.p_final.entities.User;
import com.fmi.p_final.http.AppResponse;
import com.fmi.p_final.repositories.MessageRepository;
import com.fmi.p_final.repositories.UserRepository;
import com.fmi.p_final.repositories.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }

    @Transactional
    public ResponseEntity<AppResponse<MessageDTO>> sendDirectMessage(Long senderId, Long recipientId, String content) {
        User sender = getUser(senderId);
        User recipient = getUser(recipientId);

        Message message = Message.builder()
                .sender(sender)
                .recipient(recipient)
                .content(content)
                .build();

        messageRepository.save(message);
        return AppResponse.success(new MessageDTO(sender.getId(),sender.getUsername(),MessageDTO.No_Channel, message.getContent()), "Message sent successfully.");
    }

    public ResponseEntity<AppResponse<MessageDTO>> sendMessageToChannel(Message message) {
        // Check if sender exists
        User sender = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Check if the channel exists
        Channel channel = channelRepository.findById(message.getChannel().getId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        // Create the message
        message.setTimestamp(LocalDateTime.now());

        // Save the message
        messageRepository.save(message);


        // Return success response
        return AppResponse.success(new MessageDTO(sender.getId(), sender.getUsername(), channel.getId(), message.getContent()), "Message sent successfully.");
    }


    public ResponseEntity<AppResponse<List<MessageDTO>>> getMessagesSentByUser(Long userId) {
        User user = getUser(userId);
        List<Message> messages = messageRepository.findBySender(user);

        if (messages.isEmpty()) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "No messages sent by this user.");
        }

        return AppResponse.success(messages.stream().map(
                message -> new MessageDTO(userId, user.getUsername(), message.getChannel().getId(), message.getContent())).toList(),
                "Messages retrieved successfully.");
    }

    public ResponseEntity<AppResponse<List<MessageDTO>>> getMessagesReceivedByUser(Long userId) {
        User user = getUser(userId);
        List<Message> messages = messageRepository.findByRecipient(user);

        if (messages.isEmpty()) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "No messages received by this user.");
        }

        return AppResponse.success(messages.stream().map(
                message -> new MessageDTO(userId, user.getUsername(), message.getChannel().getId(), message.getContent())).toList(),
                "Messages retrieved successfully.");
    }

    public ResponseEntity<AppResponse<List<MessageDTO>>> getMessagesInChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found."));

        List<Message> messages = messageRepository.findByChannel(channel);

        if (messages.isEmpty()) {
            return AppResponse.error(HttpStatus.NOT_FOUND, "No messages found in this channel.");
        }

        return AppResponse.success(messages.stream().map(
                message -> new MessageDTO(message.getSender().getId(),message.getSender().getUsername(),channelId, message.getContent())).toList(),
                "Messages retrieved successfully.");
    }

    public ResponseEntity<AppResponse<Message>> getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .map(message -> AppResponse.success(message, "Message found."))
                .orElseGet(() -> AppResponse.error(HttpStatus.NOT_FOUND, "Message not found."));
    }

    @Transactional
    public ResponseEntity<AppResponse<Object>> deleteMessage(Long messageId, MockedSecurity ms) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found."));

        // Ensure the requester is the sender or has bypass token
        if (!message.getSender().getId().equals(ms.requesterId) && !MockedSecutiryController.isAdmin(ms)) {
            return AppResponse.error(HttpStatus.FORBIDDEN, "You can only delete your own messages.");
        }

        message.setDeleted(true);
        messageRepository.save(message);

        return AppResponse.success(null, "Message deleted successfully.");
    }}
