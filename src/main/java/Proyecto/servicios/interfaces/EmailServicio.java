package Proyecto.servicios.interfaces;

import Proyecto.dtos.EmailDTO;

public interface EmailServicio {

    void enviarCorreo(EmailDTO emailDTO) throws Exception;
}
