package Proyecto.controllers;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Caso;
import Proyecto.servicios.implementaciones.FirebaseStorageServiceImpl;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequestMapping("/api/caso")
public class CasoController {

    @Autowired
    private CasoServicio casoServicio;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @PostMapping("/crearCaso")
    public ResponseEntity<MensajeDTO<String>> listarCasos(@RequestBody CrearCasoDTO crearCasoDTO) throws Exception {
        casoServicio.crearCaso(crearCasoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Caso creado correctamente"));

    }

    @PostMapping("/subirDocumento")
    public ResponseEntity<?> subirDocumento(@RequestBody SubirDocumentosDTO subirDocumentosDTO) throws Exception {
        casoServicio.subirDocumentos(subirDocumentosDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Documento subido correctamente"));
    }

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
}
