package Proyecto.dtos;

import java.time.LocalDateTime;

public record InfoCasosDTO(String nombreCaso, String descripcionCaso, LocalDateTime fechaInicio, String estado) {
}
