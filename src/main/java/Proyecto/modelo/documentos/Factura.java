package Proyecto.modelo.documentos;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("facturas")
public class Factura {

    @Id
    @EqualsAndHashCode.Include
    private String idFactura;

    private String concepto;
    private String descripcion;
    private float valor;
    private LocalDateTime fecha;
    private ObjectId idCaso;
}
