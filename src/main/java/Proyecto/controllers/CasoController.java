package Proyecto.controllers;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.vo.Comentario;
import Proyecto.repositorios.CasoRepo;
import Proyecto.servicios.implementaciones.FirebaseStorageServiceImpl;
import Proyecto.servicios.interfaces.ArchivoServicio;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.FirebaseStorageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequestMapping("/api/caso")
public class CasoController {

    @Autowired
    private CasoServicio casoServicio;
    @Autowired
    private ArchivoServicio archivoServicio;
    @Autowired
    private CasoRepo casoRepo;
    @Autowired
    private GridFsTemplate gridFsTemplate;


    /**
     * Crea un nuevo caso judicial a partir de los datos proporcionados.
     *
     * @param crearCasoDTO DTO con la información del nuevo caso.
     * @return Mensaje de éxito al crear el caso.
     * @throws Exception si ocurre algún error durante la creación.
     */
    @PostMapping("/crearCaso")
    public ResponseEntity<MensajeDTO<String>> crearCaso(@RequestBody CrearCasoDTO crearCasoDTO) throws Exception {
        casoServicio.crearCaso(crearCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso creado correctamente"));

    }

    /**
     * Lista los casos asignados a un abogado específico.
     *
     * @param idAbogado ID del abogado.
     * @return Lista de casos asignados al abogado.
     * @throws Exception si ocurre un error o no se encuentran casos.
     */
    @GetMapping("/listarCasosAbogado/{id}")
    public ResponseEntity<MensajeDTO<List<Caso>>> listarCasosAbogado(@PathVariable("id")String idAbogado) throws Exception {
        List<Caso> casosAbogados= casoServicio.listarCasosAbogados(idAbogado);
        return ResponseEntity.ok(new MensajeDTO<>(false,casosAbogados));

    }

    /**
     * Agrega un comentario a un caso existente.
     *
     * @param comentarCasoDTO DTO con los datos del comentario.
     * @return Mensaje de éxito al comentar el caso.
     * @throws Exception si el caso no existe o hay un error al agregar el comentario.
     */
    @PostMapping("/comentarCaso")
    public ResponseEntity<MensajeDTO<String>> listarCasosAbogado(@RequestBody ComentarCasoDTO comentarCasoDTO) throws Exception {
        casoServicio.comentarCaso(comentarCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso comentado correctamente"));

    }

    /**
     * Envía correos relacionados con un caso específico.
     *
     * @param correoCasoDTO DTO con la información para enviar el correo.
     * @return Mensaje de éxito si los correos se envían correctamente.
     * @throws Exception si ocurre un error en el envío de correos.
     */
    @PostMapping("/enviarCorreoSobrecaso")
    ResponseEntity<MensajeDTO<String>> enviarCorreoSobreCaso(@RequestBody CorreoCasoDTO correoCasoDTO) throws Exception {
        casoServicio.enviarCorreoSobreCaso(correoCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Correo/s enviado/s correctamente"));
    }

    /**
     * Actualiza la información de un caso existente.
     *
     * @param actualizarCasoDTO DTO con los nuevos datos del caso.
     * @return Mensaje de éxito al actualizar el caso.
     * @throws Exception si el caso no existe o hay errores durante la actualización.
     */
    @PutMapping("/actualizarCaso")
    ResponseEntity<MensajeDTO<String>> actualizarCaso(@RequestBody ActualizarCasoDTO actualizarCasoDTO) throws Exception {
        casoServicio.actualizarCaso(actualizarCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso Actualizado correctamente"));
    }

    /**
     * Notifica a los usuarios relacionados sobre cambios en un caso específico.
     *
     * @param idCaso ID del caso sobre el que se quiere notificar.
     * @return Mensaje de éxito si la notificación fue enviada.
     * @throws Exception si ocurre un error durante la notificación.
     */
    @PostMapping("/notificarCambios/{idCaso}")
    ResponseEntity<MensajeDTO<String>> notificarCambios(@PathVariable("idCaso") String idCaso) throws Exception {
        casoServicio.notificarCambios(idCaso);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Se ha notificado de los cambios"));
    }

    /**
     * Sube un archivo y lo asocia a un caso existente.
     *
     * @param subirDocumentosDTO ID del caso al que se desea asociar el archivo y Archivo a subir en formato multipart.
     * @return ID del documento guardado o mensaje de error.
     */
    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirYAsociarArchivo(SubirDocumentosDTO subirDocumentosDTO) {

        try {
            String idDocumento = archivoServicio.guardarArchivo(subirDocumentosDTO.archivo());

            Caso caso = casoRepo.findById(subirDocumentosDTO.idCaso()).orElseThrow();
            caso.getDocumentos().add(idDocumento);
            casoRepo.save(caso);

            return ResponseEntity.ok("Documento guardado y asociado con ID: " + idDocumento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir y asociar archivo: " + e.getMessage());
        }
    }

    /**
     * Lista los IDs de los documentos asociados a un caso.
     *
     * @param idCaso ID del caso.
     * @return Lista de IDs de documentos.
     */
    @GetMapping("/listarDocumentos/{idCaso}")
    public ResponseEntity<List<String>> listarDocumentos(@PathVariable String idCaso) {
        Caso caso = casoRepo.findById(idCaso).orElseThrow();
        return ResponseEntity.ok(caso.getDocumentos());
    }

    /**
     * Obtiene el nombre original de un documento almacenado en MongoDB GridFS.
     *
     * @param id ID del documento.
     * @return Nombre del archivo o mensaje de error.
     */
    @GetMapping("/documento/nombre/{id}")
    public ResponseEntity<String> obtenerNombreDocumento(@PathVariable String id) {
        try {
            GridFSFile archivo = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

            if (archivo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado");
            }

            return ResponseEntity.ok(archivo.getFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener nombre del archivo");
        }
    }

    /**
     * Descarga un archivo almacenado en MongoDB GridFS a partir de su ID.
     *
     * @param id ID del documento a descargar.
     * @return Archivo como recurso o mensaje de error.
     */
    @GetMapping("/descargar/{id}")
    public ResponseEntity<?> descargarArchivo(@PathVariable String id) {
        try {
            GridFSFile archivo = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

            if (archivo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado");
            }

            GridFsResource resource = gridFsTemplate.getResource(archivo);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al descargar archivo");
        }
    }

    /**
     * Lista todos los casos existentes en el sistema.
     *
     * @return Lista completa de casos.
     * @throws Exception si ocurre un error al consultar los casos.
     */
    @GetMapping("/listarTodosCasos")
    public ResponseEntity<MensajeDTO<List<Caso>>> listarDocumentos() throws Exception {
        List<Caso> casos= casoServicio.devolverTodosCasos();
        return ResponseEntity.ok(new MensajeDTO<>(false,casos));
    }

    /**
     * Endpoint para obtener la lista de comentarios asociados a un caso específico.
     *
     * @param idCaso ID del caso del cual se desean listar los comentarios.
     * @return ResponseEntity que contiene un MensajeDTO con la lista de comentarios.
     * @throws Exception si el caso no existe o ocurre algún error durante la operación.
     */
    @GetMapping("/listarComentarios/{idCaso}")
    public ResponseEntity<MensajeDTO<List<Comentario>>> listarComentarios(@PathVariable String idCaso) throws Exception {
        List<Comentario> comentarios= casoServicio.listarComentarios(idCaso);
        return ResponseEntity.ok(new MensajeDTO<>(false,comentarios));
    }

}
