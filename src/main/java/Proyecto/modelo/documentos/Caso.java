package Proyecto.modelo.documentos;

import Proyecto.modelo.enums.EstadoCaso;
import Proyecto.modelo.vo.Comentario;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime fechaInicio;
    private List<String> idCliente;
    private List<String> idAbogados;
    private EstadoCaso estadoCaso;
    private List<Comentario> comentarios;
}
