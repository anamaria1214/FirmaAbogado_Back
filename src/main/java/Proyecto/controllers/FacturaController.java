package Proyecto.controllers;

import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.vo.Abono;
import Proyecto.servicios.interfaces.FacturaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaServicio facturaServicio;

    @PostMapping("/crearFactura")
    public ResponseEntity<MensajeDTO<String>> crearCaso(@RequestBody CrearFacturaDTO crearFacturaDTO) throws Exception {
        facturaServicio.crearFactura(crearFacturaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Factura creada correctamente"));
    }

    @PutMapping("/actualizarFactura")
    ResponseEntity<MensajeDTO<String>> actualizarCaso(@RequestBody ActualizarFacturaDTO actualizarFacturaDTO) throws Exception {
        facturaServicio.actualizarFactura(actualizarFacturaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Factura Actualizada correctamente"));
    }

    @GetMapping("/listarAbonos/{id}")
    public ResponseEntity<MensajeDTO<List<Abono>>> listarCasosAbogado(@PathVariable("id")String idFactura) throws Exception {
        List<Abono> abonos= facturaServicio.getAbonosByFactura(idFactura);
        return ResponseEntity.ok(new MensajeDTO<>(false,abonos));

    }

    @GetMapping("/getFacturaPorCaso/{id}")
    public ResponseEntity<MensajeDTO<Factura>> getFacturaByCaso(@PathVariable("id")String idCaso) throws Exception {
        Factura factura= facturaServicio.getFacturaByCaso(idCaso);
        return ResponseEntity.ok(new MensajeDTO<>(false,factura));

    }
}
