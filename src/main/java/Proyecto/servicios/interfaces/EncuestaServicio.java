package Proyecto.servicios.interfaces;

import Proyecto.dtos.encuesta.EncuestaDto;

public interface EncuestaServicio {

    void responderEncuesta(EncuestaDto encuestaDto) throws Exception;
}
