package Proyecto.dtos;

import lombok.Getter;


public record InformacionCuentaDTO(String cedula,
                                   String nombre,
                                   String telefono,
                                   String direccion,
                                   String email) {
}
