package Proyecto.servicios.interfaces;

import Proyecto.dtos.*;
import Proyecto.dtos.caso.*;
import Proyecto.modelo.documentos.Caso;

import java.util.List;

public interface CasoServicio {

    List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception;

    List<InfoCasosDTO> listarCasosAbogados(String idAbogado) throws Exception;

    void crearCaso(CrearCasoDTO CrearCasoDTO) throws Exception;

    void enviarCorreoSobreCaso(CorreoCasoDTO correoCasoDTO) throws Exception;

    void subirDocumentos(SubirDocumentosDTO subirDocumentosDTO) throws Exception;

    void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception;

    void actualizarCaso(ActualizarCasoDTO actCaso) throws Exception;

    void notificarCambios(String idCaso) throws Exception;

    Caso getCasoById(String idCaso);

}
