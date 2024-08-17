package com.project1.chatRoom;

import com.project1.auditing.ApplicationAuditAware;
import com.project1.chat.ChatMessage;
import com.project1.chat.ChatMessageService;
import com.project1.chat.ChatNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chatRoom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ApplicationAuditAware auditorAware;
    private final ChatMessageService chatMessageService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<ChatRoomDto>> findChatMessages(@PathVariable String userId) {
        return ResponseEntity
                .ok(chatRoomService.findChatRooms(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ChatRoomDto> createRoom(@PathVariable String userId) {
        Integer senderId = auditorAware.getCurrentAuditor().orElseThrow(() -> new RuntimeException("Auditor ID not found"));
        return ResponseEntity
                .ok(chatRoomService.createRoom(senderId.toString(), userId));
    }

    @PostMapping("/message")
    public ResponseEntity<ChatMessage> createMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        return ResponseEntity
                .ok(savedMsg);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        System.out.println("mesagegs");
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
