package Proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("administrador")
public class Administrador {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private String email;
    private String password;


}
