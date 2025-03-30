package Proyecto.dtos;

import java.time.LocalDateTime;

public record ComentarCasoDTO(String idCaso,
                              String asunto,
                              String descripcion,
                              LocalDateTime fecha,
                              String idCuenta) {
}
