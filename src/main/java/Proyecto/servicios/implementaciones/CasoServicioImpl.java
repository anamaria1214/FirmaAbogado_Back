package Proyecto.servicios.implementaciones;

import Proyecto.dtos.InfoCasosDTO;
import Proyecto.modelo.documentos.Caso;
import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CasoServicio;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CasoServicioImpl implements CasoServicio {

    private final CasoRepo casoRepo;
    private final CuentaRepo clienteRepo;

    public CasoServicioImpl(CasoRepo casoRepo, CuentaRepo clienteRepo) {
        this.casoRepo = casoRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception {
        if(clienteRepo.findById(idCliente).isEmpty()){
            throw new Exception("Cliente no existe");
        }
        List<InfoCasosDTO> respuesta= new ArrayList<>();
        List<Caso> casos= casoRepo.buscarCasosPorClientes(idCliente);
        for(Caso caso:casos){
            respuesta.add(new InfoCasosDTO(caso.getNombreCaso(),
                    caso.getDescripcionCaso(),
                    caso.getFechaInicio(),
                    caso.getEstadoCaso().name()));
        }
        return respuesta;
    }
}
