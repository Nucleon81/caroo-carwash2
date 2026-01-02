package com.practice.project_1.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class FcmConfig {

    @PostConstruct
    public void init() throws Exception {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount =
                    getClass().getClassLoader()
                            .getResourceAsStream("firebase/caroo-wash-notificaton-firebase-adminsdk-fbsvc-a4106493f9.json");
            if (serviceAccount == null) {
                // log and skip FCM initialization instead of NPE
                System.out.println("FCM config: service account file not found, skipping Firebase init");
                return;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}
