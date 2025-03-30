package Proyecto.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public Storage firebaseStorage() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/laleydelhielo-35b3e-firebase-adminsdk-fbsvc-d844ee6ef0.json");

        return StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
                .getService();
    }
}
