package Proyecto.controllers;

import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.AgregarAbonoDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.documentos.Abono;
import Proyecto.servicios.interfaces.EstadisticasServicio;
import Proyecto.servicios.interfaces.FacturaServicio;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/factura")
public class FacturaController {

    @Autowired
    private FacturaServicio facturaServicio;
    @Autowired
    private EstadisticasServicio estadisticasServicio;

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
    @PostMapping("/agregarAbono")
    public ResponseEntity<MensajeDTO<String>> agregarAbono(@RequestBody AgregarAbonoDTO agregarAbonoDTO) throws Exception {
        String idAbono= facturaServicio.agregarAbono(agregarAbonoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,idAbono));
    }

    @PostMapping("/realizarPago/{idAbono}")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(@PathVariable("idAbono")String idAbono) throws Exception {
        Preference preference= facturaServicio.realizarPago(idAbono);
        return ResponseEntity.ok(new MensajeDTO<>(false,preference));
    }

    @PostMapping("/mercadopago/notificacion")
    public ResponseEntity<String> recibirNotificacion(@RequestBody Map<String, Object> request) {
        try {
            facturaServicio.recibirNotificacionMercadoPago(request);
            return ResponseEntity.ok("Notificación recibida correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error procesando la notificación");
        }
    }

    //Controladores para las estadísticas
    @GetMapping("/porcentajeFacturasPagadas")
    public ResponseEntity<MensajeDTO<Float>> porcentajeFacturasPagadas() throws Exception {
        float valor= estadisticasServicio.porcentajeFacturasPagadas();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));

    }
    @GetMapping("/porcentajeFacturasParciales")
    public ResponseEntity<MensajeDTO<Float>> porcentajeFacturasParciales() throws Exception {
        float valor= estadisticasServicio.porcentajeFacturasParciales();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));

    }
    @GetMapping("/porcentajeFacturasPendientes")
    public ResponseEntity<MensajeDTO<Float>> porcentajeFacturasPendientes() throws Exception {
        float valor= estadisticasServicio.porcentajeFacturasPendientes();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));

    }
    @GetMapping("/valorFacturas")
    public ResponseEntity<MensajeDTO<Float>> valorFacturas() throws Exception {
        float valor= estadisticasServicio.valorFacturas();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));
    }
    @GetMapping("/dineroRecaudado")
    public ResponseEntity<MensajeDTO<Float>> dineroRecaudado() throws Exception {
        float valor= estadisticasServicio.dineroRecaudado();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));
    }
    @GetMapping("/dineroRestante")
    public ResponseEntity<MensajeDTO<Float>> dineroRestante() throws Exception {
        float valor= estadisticasServicio.dineroRestante();
        return ResponseEntity.ok(new MensajeDTO<>(false,valor));
    }

}
