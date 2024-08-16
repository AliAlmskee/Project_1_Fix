package com.project1.chatRoom;

import com.project1.user.UserDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    @Id
    @GeneratedValue
    private String id;
    private String chatId;
    private UserDTO sender;
    private UserDTO recipient;
}
