package com.project1.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        System.out.println("FCM message sent: "+message);
        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Successfully sent message: " + response);
    }
}
