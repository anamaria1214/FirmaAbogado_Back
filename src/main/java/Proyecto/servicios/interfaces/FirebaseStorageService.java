package Proyecto.servicios.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FirebaseStorageService {

    String subirArchivo(MultipartFile file) throws IOException;
}
