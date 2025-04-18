package Proyecto.config;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIALS}")
    private String firebaseCredentialsPath;

    @Bean
    public Storage firebaseStorage() throws IOException {
        if (firebaseCredentialsPath == null || firebaseCredentialsPath.isBlank()) {
            throw new IllegalArgumentException("La ruta del archivo de configuración de Firebase no puede ser nula.");
        }

        FileInputStream serviceAccount = new FileInputStream(firebaseCredentialsPath);

        StorageOptions storageOptions = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return storageOptions.getService();
    }

}