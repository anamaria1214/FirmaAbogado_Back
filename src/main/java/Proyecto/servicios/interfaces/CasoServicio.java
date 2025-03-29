package Proyecto.servicios.interfaces;

import Proyecto.dtos.*;

import java.util.List;

public interface CasoServicio {

    List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception;

    void crearCaso(InfoCasosDTO infoCasos) throws Exception;

    void actualizarCaSo(ActualizarCasoDTO actCaso) throws Exception;

    void cambiarEstado(CambiarEstadoDTO cambiarEstado) throws Exception;

    void subirDocumentos(SubirDocumentosDTO subirDocumentosDTO) throws Exception;

    void notificarCambioEstado(NotificarCambioDTO notificarCambioDTO) throws Exception;

    void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception;
}
