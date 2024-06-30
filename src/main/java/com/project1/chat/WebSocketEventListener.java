package com.project1.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate;
//    @EventListener
//    public  void handleWebSocketDisconnectListener(
//            SessionDisconnectEvent event
//    ){
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        if(username !=null)
//        {
//            log.info("User disconnected: {}",username);
//            var chatMassage = ChatMessage.builder()
//                    .status(MessageStatus.RECEIVED)
//                    .sender(username)
//                    .build();
//            messageTemplate.convertAndSend("/topic/public",chatMassage);
//
//        }
//    }
}
