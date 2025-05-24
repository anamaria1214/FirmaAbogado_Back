package Proyecto.controllers;

import Proyecto.dtos.cuenta.CambiarPasswordDTO;
import Proyecto.dtos.cuenta.LoginDTO;
import Proyecto.dtos.MensajeDTO;
import Proyecto.dtos.cuenta.TokenDTO;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.servicios.interfaces.CuentaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {

    private final CuentaServicio cuentaServicio;

    /**
     * Endpoint para enviar un código de recuperación de contraseña al correo electrónico del usuario.
     *
     * @param email Correo electrónico del usuario que solicita el código de recuperación.
     * @return ResponseEntity con un MensajeDTO indicando si el correo fue enviado exitosamente.
     * @throws Exception si ocurre un error al enviar el código.
     */
    @PostMapping("/enviarCodigoRecuperacion/{email}")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacion(@PathVariable String email) throws Exception {
        cuentaServicio.enviarCodigoRecuperacion(email);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Correo enviado exitosamente"));
    }

    /**
     * Endpoint para realizar el inicio de sesión de un usuario.
     *
     * @param loginDTO Objeto que contiene las credenciales del usuario (email y contraseña).
     * @return ResponseEntity con un MensajeDTO que contiene el token de autenticación si las credenciales son válidas.
     * @throws Exception si las credenciales son inválidas o ocurre un error durante la autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<MensajeDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        TokenDTO tokenDTO= cuentaServicio.login(loginDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, tokenDTO));
    }

    /**
     * Endpoint para cambiar la contraseña de un usuario.
     *
     * @param cambiarPassword DTO que contiene el email del usuario, el código de recuperación y la nueva contraseña.
     * @return ResponseEntity con un MensajeDTO indicando si la contraseña fue cambiada exitosamente.
     * @throws Exception si el código es inválido o ocurre un error durante la actualización de la contraseña.
     */
    @PostMapping("/cambiarPassword")
    public ResponseEntity<MensajeDTO<String>> cambioPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPassword) throws Exception {
        cuentaServicio.cambioPassword(cambiarPassword);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Se cambio la contraseña exitosamente"));
    }

    /**
     * Endpoint para buscar una cuenta por su correo electrónico.
     *
     * @param email Correo electrónico asociado a la cuenta que se desea buscar.
     * @return ResponseEntity con un MensajeDTO que contiene la cuenta correspondiente al email proporcionado.
     * @throws Exception si no se encuentra la cuenta o si ocurre un error durante la búsqueda.
     */
    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<MensajeDTO<Cuenta>> findbyemail(@PathVariable String email) throws Exception {
        Cuenta cuenta= cuentaServicio.getCuentaByEmail(email);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuenta));
    }

    /**
     * Obtiene los datos de una cuenta a partir de su ID.
     *
     * @param idCuenta El identificador único de la cuenta a consultar.
     * @return Una {@link ResponseEntity} que contiene un {@link MensajeDTO} con la cuenta encontrada.
     * @throws Exception Si no se encuentra ninguna cuenta con el ID proporcionado.
     */
    @GetMapping("/findbyid/{idCuenta}")
    public ResponseEntity<MensajeDTO<Cuenta>> findbyIdCuenta(@PathVariable String idCuenta) throws Exception {
        Cuenta cuenta= cuentaServicio.getCuentaByIdCuenta(idCuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuenta));
    }

}
