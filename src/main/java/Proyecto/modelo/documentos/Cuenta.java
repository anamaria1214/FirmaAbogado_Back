package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.TipoCuenta;
import Proyecto.modelo.vo.CodigoValidacion;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("cuentas")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cuenta {


    @Id
    @EqualsAndHashCode.Include
    private String idCuenta;

    private List<String> especializaciones;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private String direccion;
    private TipoCuenta tipoCuenta;
    private LocalDateTime fechaCreacion;
    private CodigoValidacion codValidacionRegistro;
    private CodigoValidacion codValidacionPassword;
}
