package Proyecto.dtos.factura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActualizarFacturaDTO(@NotBlank(message="El concepto de la factura es obligatorio")
                                   String concepto,
                                   @NotBlank(message="La descripción de la factura es obligatoria")
                                   String descripcion,
                                   @NotBlank(message="El estado de la factura es obligatorio")
                                   String estado,
                                   @NotBlank(message="El valor de la factura es obligatorio")
                                   @NotNull(message="El valor de la factura es obligatorio")
                                   float valor,
                                   @NotBlank(message="La factura es obligatoria")
                                   String idFactura) {
}
