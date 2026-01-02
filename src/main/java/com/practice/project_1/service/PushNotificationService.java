package com.practice.project_1.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.practice.project_1.dto.PushNotificationRequest;
import com.practice.project_1.model.Users;
import com.practice.project_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final UserRepository userRepository;

    public int sendBroadcast(PushNotificationRequest req) {
        // build common FCM notification payload
        Notification fcmNotif = Notification.builder()
                .setTitle(req.getTitle())
                .setBody(req.getMessage())
                .build();

        List<Users> users = userRepository.findByFcmTokenNotNull();
        int successCount = 0;

        for (Users u : users) {
            String token = u.getFcmToken();
            if (token == null || token.isBlank()) continue;

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(fcmNotif)
                    .putData("type", "BROADCAST")
                    .build();
            try {
                FirebaseMessaging.getInstance().send(message);
                successCount++;
            } catch (Exception e) {
                // log error and continue; don't break broadcast for others
            }
        }
        return successCount;
    }
}
