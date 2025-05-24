package Proyecto.controllers;

import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.encuesta.EncuestaDto;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.servicios.interfaces.EncuestaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequestMapping("/api/encuesta")
public class EncuestaController {

    @Autowired
    private EncuestaServicio encuestaServicio;

    @PostMapping("/responderEncuesta")
    public ResponseEntity<MensajeDTO<String>> responderEncuesta(@RequestBody EncuestaDto encuestaDto) throws Exception {
        encuestaServicio.responderEncuesta(encuestaDto);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Encuesta enviada correctamente"));
    }
}
