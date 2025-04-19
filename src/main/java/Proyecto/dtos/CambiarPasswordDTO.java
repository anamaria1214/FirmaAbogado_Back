package Proyecto.dtos;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record CambiarPasswordDTO(String codigoVerificacion,
                                 @Email String email,
                                 @Length(min=6) String passwordNueva,
                                 @Length(min=6) String repetirContraseña) {
}