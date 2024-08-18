package com.project1.chatRoom;


import com.project1.user.UserMapper;
import com.project1.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return Optional.empty();
                });
    }

    public String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        if(chatRoomRepository.existsByChatId(chatId)){
            return chatId;
        }
        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
    private ChatRoom createChat(String senderId, String recipientId) {
        Optional<ChatRoom> exists = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(exists.isPresent()){
            return exists.get();
        }
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return senderRecipient;
    }

    public List<ChatRoomDto> findChatRooms(String userId) {
        final List<ChatRoom> chatRooms = chatRoomRepository.findAllBySenderId(userId);
        return chatRooms.stream().map(chatRoom ->
                ChatRoomDto.builder()
                        .id(chatRoom.getId())
                        .chatId(chatRoom.getChatId())
                        .sender(userMapper.toDto(userRepository.findById(Integer.valueOf(chatRoom.getSenderId())).orElseThrow()))
                        .recipient(userMapper.toDto(userRepository.findById(Integer.valueOf(chatRoom.getRecipientId())).orElseThrow()))
                        .build()
        ).toList();
    }

    public ChatRoomDto createRoom(String senderId, String userId) {
        //TODO check offer state
        final ChatRoom chatRoom = createChat(String.valueOf(senderId), userId);
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .chatId(chatRoom.getChatId())
                .sender(userMapper.toDto(userRepository.findById(Integer.parseInt(chatRoom.getSenderId())).orElseThrow()))
                .recipient(userMapper.toDto(userRepository.findById(Integer.parseInt(chatRoom.getRecipientId())).orElseThrow()))
                .build();
    }
}