package Proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("clientes")
public class Cliente {

    @Id
    @EqualsAndHashCode.Include
    private String idCliente;

    private String nombre;
    private String telefono;
    private String direccion;
}
