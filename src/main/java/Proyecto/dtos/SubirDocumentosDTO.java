package Proyecto.dtos;

import org.springframework.web.multipart.MultipartFile;

public record SubirDocumentosDTO(String idCaso, MultipartFile archivo) {
}
