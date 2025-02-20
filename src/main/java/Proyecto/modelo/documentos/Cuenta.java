package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.TipoCuenta;
import Proyecto.modelo.vo.CodigoValidacion;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("cuentas")
public class Cuenta {

    @Id
    @EqualsAndHashCode.Include
    private String idCuenta;

    private String email;
    private String password;
    private TipoCuenta tipoCuenta;
    private CodigoValidacion codValidacionRegistro;
}
