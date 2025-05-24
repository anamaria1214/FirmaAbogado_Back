package Proyecto.modelo.documentos;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document("encuestas")
public class Encuesta {

    @Id
    @EqualsAndHashCode.Include
    private String codigo;

    //Foreign keys
    private String idCaso;
    private String idCuenta;

    //valores de la encuenta
    private int gustoCaso;  //¿Obtuvo una resolución satisfactoria del caso?
    private int gustoAbogado; //¿Estuvo satisfech@ con el cómo el abogado manejó su caso?
    private int gustoFirma; //¿Estuvo satisfech@ con el manejo de la forma con su caso?
    private int gustoPagina; //¿Se sintió satisfecho con la página web para manejar su caso y cuenta?
    private String comentarioAdicional;
}
