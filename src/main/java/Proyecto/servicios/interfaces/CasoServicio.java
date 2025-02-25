package Proyecto.servicios.interfaces;

import Proyecto.dtos.InfoCasosDTO;

import java.util.List;

public interface CasoServicio {

    List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception;
}
