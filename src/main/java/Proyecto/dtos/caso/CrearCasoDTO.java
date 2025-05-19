package Proyecto.dtos.caso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CrearCasoDTO(
        @NotBlank(message="El nombre del caso es obligatorio")
        String nombreCaso,
        @NotNull(message="La descripción del caso es obligatorio")
        String descripcion,
        @NotBlank(message="La fecha de apertura del caso es obligatorio")
        LocalDateTime fechaCreacion,
        List<String> clientes,
        List<String> abogados,
        @NotBlank(message="El estado del caso es obligatorio")
        String estado) {
}
