package Proyecto.servicios.implementaciones;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.modelo.enums.EstadoCaso;
import Proyecto.modelo.vo.Comentario;
import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EmailServicio;
import Proyecto.servicios.interfaces.FirebaseStorageService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CasoServicioImpl implements CasoServicio {

    private final CasoRepo casoRepo;
    private final CuentaServicio cuentaServicio;
    private final CuentaRepo clienteRepo;
    private final FirebaseStorageService firebaseStorageService;
    private final EmailServicio emailServicio;

    public CasoServicioImpl(CasoRepo casoRepo, CuentaServicio cuentaServicio, CuentaRepo clienteRepo, FirebaseStorageService firebaseStorageService, EmailServicio emailServicio) {
        this.casoRepo = casoRepo;
        this.cuentaServicio = cuentaServicio;
        this.clienteRepo = clienteRepo;
        this.firebaseStorageService = firebaseStorageService;
        this.emailServicio = emailServicio;
    }

    /**
     * Lista los casos asociados a un cliente específico.
     * @param idCliente Identificador único del cliente.
     * @return Lista de objetos InfoCasosDTO con la información de los casos del cliente.
     * @throws Exception Si el cliente no existe en la base de datos.
     */
    @Override
    public List<Caso> listarCasosClientes(String idCliente) throws Exception {
        if(clienteRepo.findByCedula(idCliente).isEmpty()){
            throw new Exception("Cliente no existe");
        }
        return casoRepo.buscarCasosPorClientes(idCliente);
    }

    /**
     * Lista los casos en los que un abogado específico está involucrado.
     *
     * @param idAbogado ID del abogado cuyos casos se quieren listar.
     * @return Lista de InfoCasosDTO con la información de los casos asociados al abogado.
     * @throws Exception Si ocurre algún error durante la consulta.
     */
    @Override
    public List<Caso> listarCasosAbogados(String idAbogado) throws Exception {
        if(clienteRepo.findByCedula(idAbogado).isEmpty()){
            throw new Exception("Cliente no existe");
        }
        return casoRepo.buscarCasosPorAbogado(idAbogado);
    }

    /**
     * Crea un nuevo caso legal en el sistema con la información proporcionada.
     * @param crearCasoDTO Objeto DTO con la información del caso a crear.
     * @throws Exception Si los datos proporcionados son inválidos o si los clientes/abogados no existen.
     */
    @Override
    public void crearCaso(CrearCasoDTO crearCasoDTO) throws Exception {
        if(crearCasoDTO.nombreCaso().isEmpty() || crearCasoDTO.descripcion().isEmpty() || crearCasoDTO.abogados().isEmpty() || crearCasoDTO.clientes().isEmpty()){
            throw new Exception("Campos obligatorios vacios");
        }
        ArrayList<Comentario> comentarios= new ArrayList<>();
        ArrayList<String> documentos= new ArrayList<>();

        Caso caso= new Caso();
        caso.setComentarios(comentarios);
        caso.setDocumentos(documentos);
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

    /**
     * Actualiza la información de un caso existente.
     *
     * @param actCaso DTO que contiene los datos actualizados del caso.
     * @throws Exception si el caso no se encuentra en la base de datos.
     */
    @Override
    public void actualizarCaso(ActualizarCasoDTO actCaso) throws Exception {
        Optional<Caso> casoOptional= casoRepo.findById(actCaso.idCaso());
        if(casoOptional.isEmpty()){
            throw new Exception("Caso no encontrado");
        }
        Caso caso = casoOptional.get();

        caso.setNombreCaso(actCaso.nombreCaso());
        caso.setDescripcionCaso(actCaso.descripcionCaso());
        caso.setEstadoCaso(EstadoCaso.valueOf(actCaso.estadoCaso()));
        caso.setIdCliente(actCaso.clientes());
        caso.setIdAbogados(actCaso.abogados());

        casoRepo.save(caso);
    }

    /**
     * Sube un documento al almacenamiento y lo asocia al caso correspondiente.
     *
     * @param subirDocumentosDTO DTO que contiene el ID del caso y el archivo a subir.
     * @throws Exception si el caso no se encuentra.
     */
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

    /**
     * Notifica por correo electrónico a los clientes asociados a un caso sobre cambios realizados.
     *
     * @param idCaso ID del caso que tuvo modificaciones.
     * @throws Exception si el caso no se encuentra.
     */
    @Override
    public void notificarCambios(String idCaso) throws Exception {
        Optional<Caso> casoOptional= casoRepo.findById(idCaso);
        if(casoOptional.isEmpty()){
            throw new Exception("Caso no encontrado");
        }
        Caso caso = casoOptional.get();
        String cuerpo= "El caso con el nombre: "+caso.getNombreCaso()+" " +
                "tuvo cambios. A continuación se mostrará el estado actual de su caso." +
                "Nombre :"+caso.getNombreCaso()+
                "Descripción: "+caso.getDescripcionCaso()+
                "Estado: "+caso.getEstadoCaso();

        for(String cliente: caso.getIdCliente()){
            Cuenta cuenta= cuentaServicio.getCuentaByCedula(cliente);
            EmailDTO emailDTO= new EmailDTO("Cambio en el caso: "+caso.getNombreCaso(), cuerpo, cuenta.getEmail());
            emailServicio.enviarCorreo(emailDTO);
        }

    }

    /**
     * Devuelve la lista de todos los casos almacenados en la base de datos.
     *
     * @return Lista de objetos {@link Caso}.
     * @throws Exception si ocurre un error al acceder a los datos desde el repositorio.
     */
    @Override
    public List<Caso> devolverTodosCasos() throws Exception {
        return casoRepo.findAll();
    }

    /**
     * Lista todos los comentarios asociados a un caso específico.
     *
     * @param idCaso ID del caso del cual se quieren obtener los comentarios.
     * @return Lista de objetos {@link Comentario} pertenecientes al caso.
     * @throws Exception si el caso con el ID proporcionado no existe.
     */
    @Override
    public List<Comentario> listarComentarios(String idCaso) throws Exception {
        Optional<Caso> casoOptional= casoRepo.findById(idCaso);
        if(casoOptional.isEmpty()) {
            throw new Exception("No existe el caso");
        }
        return casoOptional.get().getComentarios();
    }

    /**
     * Envía un correo personalizado a los clientes relacionados con un caso específico.
     *
     * @param correoCasoDTO DTO que contiene el ID del caso, el asunto y el cuerpo del correo.
     * @throws Exception si el caso o alguno de los clientes no se encuentra.
     */
    @Override
    public void enviarCorreoSobreCaso(CorreoCasoDTO correoCasoDTO) throws Exception {
        Optional<Caso> casoOptional = casoRepo.findById(correoCasoDTO.idCaso());
        if(casoOptional.isEmpty()){
            throw new Exception("Caso no existente");
        }
        Caso caso = casoOptional.get();
        for(int i=0;i<caso.getIdCliente().size();i++){
            Optional<Cuenta> cuenta = clienteRepo.findByCedula(caso.getIdCliente().get(i));
            if(cuenta.isEmpty()){
                throw new Exception("Cliente no encontrado");
            }
            EmailDTO emailDTO= new EmailDTO(correoCasoDTO.asunto(), "Correo enviado acerca del caso "+caso.getNombreCaso()+ ": "+correoCasoDTO.cuerpo(), cuenta.get().getEmail());
            emailServicio.enviarCorreo(emailDTO);
        }

    }

    /**
     * Agrega un comentario a un caso determinado.
     *
     * @param comentarCasoDTO DTO con la información del comentario a agregar.
     * @throws Exception si el caso no se encuentra.
     */
    @Override
    public void comentarCaso(ComentarCasoDTO comentarCasoDTO) throws Exception {
        if(comentarCasoDTO.asunto().isEmpty() || comentarCasoDTO.descripcion().isEmpty()){
            throw new Exception("Campos obligatorios vacios");
        }
        Optional<Caso> casoOpt = casoRepo.findById(comentarCasoDTO.idCaso());
        if (casoOpt.isEmpty()) {
            throw new Exception("Caso no encontrado.");
        }

        Caso caso = casoOpt.get();

        Comentario comentario= new Comentario(new ObjectId().toHexString(),
                comentarCasoDTO.asunto(),
                comentarCasoDTO.descripcion(),
                LocalDateTime.now(),
                comentarCasoDTO.idCuenta());

        caso.getComentarios().add(comentario);
        casoRepo.save(caso);
    }
}

