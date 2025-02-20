package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.EstadoCaso;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("casos")
public class Caso {

    @Id
    @EqualsAndHashCode.Include
    private String codigo;

    private String nombreCaso;
    private String descripcionCaso;
    private List<ObjectId> idCliente;
    private List<ObjectId> idAbogados;
    private EstadoCaso estadoCaso;
}
