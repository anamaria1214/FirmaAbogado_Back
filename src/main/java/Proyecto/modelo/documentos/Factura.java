package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.EstadoFactura;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

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
    private EstadoFactura estadoFactura;
    private float valor;
    private LocalDateTime fecha;
    private List<String> abonos;
    private String idCaso;
    private float saldoPendiente;



}
