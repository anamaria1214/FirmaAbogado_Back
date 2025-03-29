package Proyecto.servicios.implementaciones;

import Proyecto.dtos.*;
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

    //Método para mostrar todos los casos que pertenecen a un cliente
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

    @Override
    public void crearCaso(InfoCasosDTO infoCasos) throws Exception {

    }

    @Override
    public void actualizarCaSo(ActualizarCasoDTO actCaso) throws Exception {

    }

    @Override
    public void cambiarEstado(CambiarEstadoDTO cambiarEstado) throws Exception {

    }

    @Override
    public void subirDocumentos(SubirDocumentosDTO subirDocumentosDTO) throws Exception {

    }

    @Override
    public void notificarCambioEstado(NotificarCambioDTO notificarCambioDTO) throws Exception {

    }

    @Override
    public void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception {

    }
}
