package Proyecto.servicios.implementaciones;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.modelo.enums.EstadoCaso;
import Proyecto.modelo.vo.Comentario;
import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.FirebaseStorageService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CasoServicioImpl implements CasoServicio {

    private final CasoRepo casoRepo;
    private final CuentaRepo clienteRepo;
    private final FirebaseStorageService firebaseStorageService;

    public CasoServicioImpl(CasoRepo casoRepo, CuentaRepo clienteRepo, FirebaseStorageService firebaseStorageService) {
        this.casoRepo = casoRepo;
        this.clienteRepo = clienteRepo;
        this.firebaseStorageService = firebaseStorageService;
    }

    /**
     * Lista los casos asociados a un cliente específico.
     * @param idCliente Identificador único del cliente.
     * @return Lista de objetos InfoCasosDTO con la información de los casos del cliente.
     * @throws Exception Si el cliente no existe en la base de datos.
     */
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

    /**
     * Lista los casos en los que un abogado específico está involucrado.
     *
     * @param idAbogado ID del abogado cuyos casos se quieren listar.
     * @return Lista de InfoCasosDTO con la información de los casos asociados al abogado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public List<InfoCasosDTO> listarCasosAbogados(String idAbogado) throws Exception {
        List<InfoCasosDTO> infoCasos= new ArrayList<>();
        List<Caso> casos= casoRepo.buscarCasosPorAbogado(idAbogado);
        for(Caso caso: casos){
            infoCasos.add(new InfoCasosDTO(caso.getNombreCaso(),
                    caso.getDescripcionCaso(),
                    caso.getFechaInicio(),
                    caso.getEstadoCaso().name()));
        }
        return infoCasos;
    }


    /**
     * Crea un nuevo caso legal en el sistema con la información proporcionada.
     * @param crearCasoDTO Objeto DTO con la información del caso a crear.
     * @throws Exception Si los datos proporcionados son inválidos o si los clientes/abogados no existen.
     */
    @Override
    public void crearCaso(CrearCasoDTO crearCasoDTO) throws Exception {
        Caso caso= new Caso();
        caso.setNombreCaso(crearCasoDTO.nombreCaso());
        caso.setDescripcionCaso(crearCasoDTO.descripcion());
        caso.setFechaInicio(crearCasoDTO.fechaCreacion());
        if(crearCasoDTO.estado().equals("ACTIVO")){
            caso.setEstadoCaso(EstadoCaso.ACTIVO);
        }else if(crearCasoDTO.estado().equals("INACTIVO")){
            caso.setEstadoCaso(EstadoCaso.INACTIVO);
        }else if(crearCasoDTO.estado().equals("CERRADO")){
            caso.setEstadoCaso(EstadoCaso.CERRADO);
        }else{
            throw new Exception("El estado indicado no es valido");
        }
        List<String> cedulasClientes= new ArrayList<>();
        for(int i=0; i<crearCasoDTO.clientes().size();i++){
            Optional<Cuenta> cuenta= clienteRepo.findByCedula(crearCasoDTO.clientes().get(i));
            if(cuenta.isPresent()){
                cedulasClientes.add(crearCasoDTO.clientes().get(i));
            }else{
                throw new Exception("La cédula "+crearCasoDTO.clientes().get(i)+" no pertenece a ningún cliente registrado");
            }
        }
        caso.setIdCliente(cedulasClientes);

        List<String> cedulasAbogados= new ArrayList<>();
        for(int i=0; i<crearCasoDTO.abogados().size();i++){
            Optional<Cuenta> cuenta= clienteRepo.findByCedula(crearCasoDTO.abogados().get(i));
            if(cuenta.isPresent()){
                cedulasAbogados.add(crearCasoDTO.abogados().get(i));
            }else{
                throw new Exception("La cédula "+crearCasoDTO.abogados().get(i)+" no pertenece a ningún abogado registrado");
            }
        }
        caso.setIdAbogados(cedulasAbogados);

        casoRepo.save(caso);
    }

    @Override
    public void actualizarCaSo(ActualizarCasoDTO actCaso) throws Exception {

    }

    @Override
    public void cambiarEstado(CambiarEstadoDTO cambiarEstado) throws Exception {

    }

    @Override
    public void subirDocumentos(SubirDocumentosDTO subirDocumentosDTO) throws Exception {
        Optional<Caso> casoOpt = casoRepo.findById(subirDocumentosDTO.idCaso());
        if (casoOpt.isEmpty()) {
            throw new Exception("Caso no encontrado.");
        }
        String urlArchivo = firebaseStorageService.subirArchivo(subirDocumentosDTO.archivo());

        Caso caso = casoOpt.get();
        caso.getDocumentos().add(urlArchivo);
        casoRepo.save(caso);
    }

    @Override
    public void notificarCambioEstado(NotificarCambioDTO notificarCambioDTO) throws Exception {

    }

    @Override
    public void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception {
        Optional<Caso> casoOpt = casoRepo.findById(comentarCasoDTO.idCaso());
        if (casoOpt.isEmpty()) {
            throw new Exception("Caso no encontrado.");
        }

        Caso caso = casoOpt.get();

        Comentario comentario= new Comentario(new ObjectId().toHexString(),
                comentarCasoDTO.asunto(),
                comentarCasoDTO.descripcion(),
                comentarCasoDTO.fecha(),
                comentarCasoDTO.idCuenta());

        caso.getComentarios().add(comentario);
        casoRepo.save(caso);
    }
}
