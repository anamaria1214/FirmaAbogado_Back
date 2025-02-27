package Proyecto.controllers;

import Proyecto.dtos.CambiarPasswordDTO;
import Proyecto.dtos.MensajeDTO;
import Proyecto.servicios.interfaces.CuentaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final CuentaServicio cuentaServicio;

    @PostMapping("/cambiarPassword")
    public ResponseEntity<MensajeDTO<String>> cambioPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPassword) throws Exception {
        cuentaServicio.cambioPassword(cambiarPassword);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se cambio la contraseña exitosamente"));
    }

}
