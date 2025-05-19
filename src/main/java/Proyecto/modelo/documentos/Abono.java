package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.EstadoPago;
import Proyecto.modelo.vo.Pago;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("abonos")
public class Abono {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private LocalDateTime fecha;
    private float monto;
    private Pago pago;
    private String codigoPasarela;
}
