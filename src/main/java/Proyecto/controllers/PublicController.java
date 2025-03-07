package Proyecto.controllers;

import Proyecto.dtos.CambiarPasswordDTO;
import Proyecto.dtos.LoginDTO;
import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.TokenDTO;
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

    @PostMapping("/enviarCodigoRecuperacion/{email}")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacion(@PathVariable String email) throws Exception {
        cuentaServicio.enviarCodigoRecuperacion(email);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Correo enviado exitosamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        TokenDTO tokenDTO= cuentaServicio.login(loginDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, tokenDTO));
    }

    @PostMapping("/cambiarPassword")
    public ResponseEntity<MensajeDTO<String>> cambioPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPassword) throws Exception {
        cuentaServicio.cambioPassword(cambiarPassword);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se cambio la contraseña exitosamente"));
    }

}
