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
@Document("comentarios")
public class Comentario {

    @Id
    @EqualsAndHashCode.Include
    private String idComentario;

    private String asunto;
    private String descripcion;
    private LocalDateTime fecha;
    private ObjectId idCuenta;
}
