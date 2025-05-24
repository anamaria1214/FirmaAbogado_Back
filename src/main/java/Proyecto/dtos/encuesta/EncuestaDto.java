package Proyecto.dtos.encuesta;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EncuestaDto(@NotBlank(message="El caso es obligatorio")
                          String idCaso,
                          @NotBlank(message="La cuenta es obligatoria")
                          @Min(value=1, message = "El mínimo valor es 1")
                          @Max(value=5, message = "El máximo valor es 5")
                          String cedula,

                          @Min(value=1, message = "El mínimo valor es 1")
                          @Max(value=5, message = "El máximo valor es 5")
                          @NotBlank(message="Este campo es obligatorio")
                          int gustoCaso,

                          @Min(value=1, message = "El mínimo valor es 1")
                          @Max(value=5, message = "El máximo valor es 5")
                          @NotBlank(message="Este campo es obligatorio")
                          int gustoAbogado,

                          @Min(value=1, message = "El mínimo valor es 1")
                          @Max(value=5, message = "El máximo valor es 5")
                          @NotBlank(message="Este campo es obligatorio")
                          int gustoFirma,

                          @Min(value=1, message = "El mínimo valor es 1")
                          @Max(value=5, message = "El máximo valor es 5")
                          @NotBlank(message="Este campo es obligatorio")
                          int gustoPagina,
                          String comentarioAdicional) {
}
