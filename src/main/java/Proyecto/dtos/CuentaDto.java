package Proyecto.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CuentaDto {
        private String cedula;
        private String nombre;
        private String telefono;
        private String email;
        private String direccion;
        private String password; // Solo en DTO de entrada
        private String confirmarContrasenia;
        private String rol; // CLIENTE, ABOGADO, ADMIN

}
