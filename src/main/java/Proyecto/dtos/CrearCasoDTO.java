package Proyecto.dtos;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record CrearCasoDTO(
        @NotBlank(message="El nombre del caso es obligatorio")
        String nombreCaso,
        @NotBlank(message="La descripción del caso es obligatorio")
        String descripcion,
        @NotBlank(message="La fecha de apertura del caso es obligatorio")
        LocalDateTime fechaCreacion,
        List<String> clientes,
        List<String> abogados,
        @NotBlank(message="El estado del caso es obligatorio")
        String estado) {
}
