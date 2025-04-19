package Proyecto.controllers;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Caso;
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


    @PostMapping("/crearCaso")
    public ResponseEntity<MensajeDTO<String>> crearCaso(@RequestBody CrearCasoDTO crearCasoDTO) throws Exception {
        casoServicio.crearCaso(crearCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso creado correctamente"));

    }

    /*
    @PostMapping("/subirDocumento")
    public ResponseEntity<?> subirDocumento(@RequestBody SubirDocumentosDTO subirDocumentosDTO) throws Exception {
        casoServicio.subirDocumentos(subirDocumentosDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Documento subido correctamente"));
    }*/

    @GetMapping("/listarCasosAbogado/{id}")
    public ResponseEntity<MensajeDTO<List<InfoCasosDTO>>> listarCasosAbogado(@PathVariable("id")String idAbogado) throws Exception {
        List<InfoCasosDTO> casosAbogados= casoServicio.listarCasosAbogados(idAbogado);
        return ResponseEntity.ok(new MensajeDTO<>(false,casosAbogados));

    }

    @PostMapping("/comentarCaso")
    public ResponseEntity<MensajeDTO<String>> listarCasosAbogado(@RequestBody ComentarCasoDTO comentarCasoDTO) throws Exception {
        casoServicio.comentarCaso(comentarCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso comentado correctamente"));

    }

    @PostMapping("/enviarCorreoSobrecaso")
    ResponseEntity<MensajeDTO<String>> enviarCorreoSobreCaso(@RequestBody CorreoCasoDTO correoCasoDTO) throws Exception {
        casoServicio.enviarCorreoSobreCaso(correoCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Correo/s enviado/s correctamente"));
    }

    @PutMapping("/actualizarCaso")
    ResponseEntity<MensajeDTO<String>> actualizarCaso(@RequestBody ActualizarCasoDTO actualizarCasoDTO) throws Exception {
        casoServicio.actualizarCaso(actualizarCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso Actualizado correctamente"));
    }

    @PostMapping("/notificarCambios/{idCaso}")
    ResponseEntity<MensajeDTO<String>> notificarCambios(@PathVariable("idCaso") String idCaso) throws Exception {
        casoServicio.notificarCambios(idCaso);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Se ha notificado de los cambios"));
    }
    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> subirYAsociarArchivo(
            @RequestParam("idCaso") String idCaso,
            @RequestParam("archivo") MultipartFile archivo) {

        try {
            String idDocumento = archivoServicio.guardarArchivo(archivo);

            Caso caso = casoRepo.findById(idCaso).orElseThrow();
            caso.getDocumentos().add(idDocumento);
            casoRepo.save(caso);

            return ResponseEntity.ok("Documento guardado y asociado con ID: " + idDocumento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir y asociar archivo: " + e.getMessage());
        }
    }

    @GetMapping("/listarDocumentos/{idCaso}")
    public ResponseEntity<List<String>> listarDocumentos(@PathVariable String idCaso) {
        Caso caso = casoRepo.findById(idCaso).orElseThrow();
        return ResponseEntity.ok(caso.getDocumentos());
    }

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
}
