package com.apistore.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(
                                    new ClassPathResource("api-store-17925-firebase-adminsdk-fbsvc-d30ee81a40.json").getInputStream()
                            )
                    )
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("🔥 Firebase Admin SDK initialized successfully!");
        }
    }
}
