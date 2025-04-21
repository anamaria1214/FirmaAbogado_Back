package Proyecto.dtos;

import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

public record SubirDocumentosDTO(String idCaso, MultipartFile archivo) {
}
