package Proyecto.servicios.implementaciones;

import Proyecto.dtos.InfoCasosDTO;
import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cliente;
import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.ClienteRepo;
import Proyecto.servicios.interfaces.CasoServicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CasoServicioImpl implements CasoServicio {

    private final CasoRepo casoRepo;
    private final ClienteRepo clienteRepo;

    public CasoServicioImpl(CasoRepo casoRepo, ClienteRepo clienteRepo) {
        this.casoRepo = casoRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public List<InfoCasosDTO> listarCasosClientes(String idCliente) throws Exception {
        if(clienteRepo.buscarCliente(idCliente).isEmpty()){
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
