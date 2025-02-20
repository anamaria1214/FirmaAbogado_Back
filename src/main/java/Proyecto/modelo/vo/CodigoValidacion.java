package Proyecto.modelo.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CodigoValidacion {

    private LocalDateTime fechaCreacion;
    private String codigo;
}

