package Proyecto.dtos;

import java.util.List;

public record ActualizarCasoDTO(String idCaso,
                                String nombreCaso,
                                String descripcionCaso,
                                String estadoCaso,
                                List<String> clientes,
                                List<String> abogados) {
}
