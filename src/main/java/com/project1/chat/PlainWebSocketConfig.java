package com.project1.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
//
//@Configuration
//public class PlainWebSocketConfig implements WebSocketConfigurer {
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(new TextWebSocketHandler() {
//            @Override
//            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//                System.out.println("Plain WebSocket connection established");
//            }
//
//            @Override
//            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//                System.out.println("Received plain message: " + message.getPayload());
//                session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
//            }
//        }, "/plain-ws").setAllowedOrigins("*");
//    }
//}
