package Proyecto.servicios.implementaciones;

import Proyecto.dtos.encuesta.EncuestaDto;
import Proyecto.modelo.documentos.Encuesta;
import Proyecto.repositorios.EncuestaRepo;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EncuestaServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EncuestaServicioImpl implements EncuestaServicio {

    private final EncuestaRepo encuestaRepo;
    private final CuentaServicio cuentaServicio;
    private final CasoServicio casoServicio;

    public EncuestaServicioImpl(EncuestaRepo encuestaRepo, CuentaServicio cuentaServicio, CasoServicio casoServicio) {
        this.encuestaRepo = encuestaRepo;
        this.cuentaServicio = cuentaServicio;
        this.casoServicio = casoServicio;
    }

    @Override
    public void responderEncuesta(EncuestaDto encuestaDto) throws Exception {
        Encuesta encuesta= new Encuesta();

        if(cuentaServicio.getCuentaById(encuestaDto.cedula())==null){
            throw new Exception("La cuenta no existe");
        }
        if(casoServicio.getCasoById(encuestaDto.idCaso())==null){
            throw new Exception("El caso no existe");
        }

        encuesta.setIdCaso(encuestaDto.idCaso());
        encuesta.setIdCuenta(encuestaDto.cedula());

        encuesta.setGustoCaso(encuestaDto.gustoCaso());
        encuesta.setGustoAbogado(encuestaDto.gustoAbogado());
        encuesta.setGustoFirma(encuestaDto.gustoFirma());
        encuesta.setGustoPagina(encuestaDto.gustoPagina());

        encuesta.setComentarioAdicional(encuestaDto.comentarioAdicional());

        encuestaRepo.save(encuesta);
    }
}
