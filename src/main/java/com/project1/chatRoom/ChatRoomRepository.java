package com.project1.chatRoom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

    List<ChatRoom> findAllBySenderId(String senderId);

    boolean existsByChatId(String chatId);

    Optional<ChatRoom> findByChatId(String chatId);
}
