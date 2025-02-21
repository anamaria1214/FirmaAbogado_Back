package Proyecto.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegistroDTO(@NotBlank @Email String email,
                          @NotBlank @Length(min=8) String password,
                          String rol) {
}
