package Proyecto.dtos;

import Proyecto.modelo.enums.TipoCuenta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.message.Message;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record CuentaAbogadoDTO(
                               @NotBlank(message="La cédula es obligatoria") @Length(min=5)
                               String cedula,
                               List<String>especializaciones,
                               @NotBlank (message="El nombre es obligatorio") @Length(min=3)
                               String nombre,
                               @NotBlank(message="El teléfono es obligatorio") @Length(min=7)
                               String telefono,
                               @NotBlank(message="El correo es obligatorio") @Email(message="No es un correo valido")
                               String email,
                               @NotBlank(message="La dirección es obligatoria")
                               String direccion,
                               @NotBlank(message="La dirección es obligatoria")
                               TipoCuenta tipoCuenta,
                               @NotNull(message="La fecha es obligatoria")
                               LocalDateTime fechaCreacion) {
}
