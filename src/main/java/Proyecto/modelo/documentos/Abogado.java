package Proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("abogados")
public class Abogado {

    @Id
    @EqualsAndHashCode.Include
    private String idAbogado;

    private String nombre;
    private List<String> especializaciones;
    private String telefono;
}
