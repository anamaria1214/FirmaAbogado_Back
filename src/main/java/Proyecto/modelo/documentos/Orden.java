package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.EstadoOrden;
import Proyecto.modelo.vo.Pago;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("ordenes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Orden {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idCaso;
    private LocalDateTime fecha;
    private String CodigoPasarela;
    private Pago pago;
    private float Total;
    private EstadoOrden estadoOrden;

}