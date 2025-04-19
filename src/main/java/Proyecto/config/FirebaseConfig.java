package Proyecto.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;


public class FirebaseConfig {

        @Bean
        public FirebaseApp firebaseApp() throws IOException {
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("laleydelhielo-35b3e-firebase-adminsdk-fbsvc-d844ee6ef0.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("laleydelhielo-35b3e.firebasestorage.app") // Reemplaza con tu bucket
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            }
            return FirebaseApp.getInstance();
        }

        /*
        @Bean
        public Storage firebaseStorage() throws IOException {
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase-credentials.json");

            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                    .getService();
        }*/
    }


