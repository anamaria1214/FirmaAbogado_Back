package Proyecto.servicios.interfaces;

import Proyecto.dtos.*;

import java.util.List;

public interface CasoServicio {

    List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception;

    List<InfoCasosDTO> listarCasosAbogados(String idAbogado) throws Exception;

    void crearCaso(CrearCasoDTO CrearCasoDTO) throws Exception;

    void actualizarCaSo(ActualizarCasoDTO actCaso) throws Exception;

    void cambiarEstado(CambiarEstadoDTO cambiarEstado) throws Exception;

    void subirDocumentos(SubirDocumentosDTO subirDocumentosDTO) throws Exception;

    void notificarCambioEstado(NotificarCambioDTO notificarCambioDTO) throws Exception;

    void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception;
}
