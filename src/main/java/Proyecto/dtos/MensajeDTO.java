package Proyecto.dtos;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}

