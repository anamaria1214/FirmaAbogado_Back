package Proyecto.controllers;

import Proyecto.dtos.CrearCasoDTO;
import Proyecto.dtos.InfoCasosDTO;
import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.SubirDocumentosDTO;
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

    @PostMapping("/{idCaso}/subir-documento")
    public ResponseEntity<?> subirDocumento(@RequestBody SubirDocumentosDTO subirDocumentosDTO) throws Exception {
        casoServicio.subirDocumentos(subirDocumentosDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Documento subido correctamente"));
    }
}
