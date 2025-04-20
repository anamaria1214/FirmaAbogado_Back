package Proyecto.controllers;


import Proyecto.dtos.*;

import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cuenta;

import Proyecto.servicios.interfaces.CasoServicio;

import Proyecto.servicios.interfaces.CuentaServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "https://laleydelhielo.vercel.app",  allowCredentials = "true")
@RestController
@RequestMapping("/api/cuenta")
public class CuentaViewController {
    @Autowired
    private CuentaServicio cuentaServicio;
    @Autowired
    private CasoServicio casoServicio;


    /**
     * Crea una nueva cuenta a partir de los datos recibidos en el cuerpo de la solicitud.
     *
     * @param cuentaDto Objeto con los datos de la cuenta a crear.
     * @return Objeto {@link CuentaDto} con la información de la cuenta creada.
     * @throws Exception si ocurre un error durante la creación.
     */
    @PostMapping("/crear")
    public CuentaDto crearCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.crearCuenta(cuentaDto);
    }

    /**
     * Actualiza una cuenta existente con los datos proporcionados.
     *
     * @param cuentaDto Objeto con la información actualizada de la cuenta.
     * @return Objeto {@link CuentaDto} actualizado.
     * @throws Exception si la cuenta no existe o falla la operación.
     */
    @PutMapping("/actualizar" )
    public CuentaDto actualizarCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.actualizarCuenta(cuentaDto);
    }

    /**
     * Elimina una cuenta (por ejemplo, un administrador) a partir de su ID.
     *
     * @param id Identificador único de la cuenta a eliminar.
     * @throws Exception si la cuenta no existe o falla la eliminación.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws Exception {
        CuentaDto clientDelete = cuentaServicio.getCuentaById(id);
        cuentaServicio.eliminarCuenta(clientDelete);
    }

    /**
     * Lista todos los casos asociados a un cliente específico.
     *
     * @param idCliente ID del cliente del cual se desean obtener los casos.
     * @return {@link ResponseEntity} con un mensaje que incluye la lista de casos del cliente.
     * @throws Exception si el cliente no existe o hay un error al consultar los casos.
     */
    @GetMapping("/listarCasos/{id}")
    public ResponseEntity<MensajeDTO<List<Caso>>> listarCasos(@PathVariable("id")String idCliente) throws Exception {
        List<Caso> casosCliente= casoServicio.listarCasosClientes(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false,casosCliente));

    }

    /**
     * Crea una cuenta de abogado con la información recibida en el cuerpo de la solicitud.
     *
     * @param cuentaAbogadoDTO Objeto con los datos del abogado a crear.
     * @return {@link ResponseEntity} con un mensaje de éxito.
     * @throws Exception si hay un error durante la creación.
     */
    @PostMapping("/crearAbogado")
    public ResponseEntity<MensajeDTO<String>> crearCuentaAbogado(@RequestBody CuentaAbogadoDTO cuentaAbogadoDTO) throws Exception {
        cuentaServicio.crearCuentaAbogado(cuentaAbogadoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Abogado creado correctamente"));
    }

    /**
     * Obtiene una cuenta a partir de su número de cédula.
     *
     * @param cedula Número de cédula del usuario.
     * @return {@link ResponseEntity} con la cuenta correspondiente.
     * @throws Exception si no se encuentra la cuenta.
     */
    @GetMapping("/getCuentaByCedula/{cedula}")
    public ResponseEntity<MensajeDTO<Cuenta>> getCuentaByCedula(@PathVariable("cedula")String cedula) throws Exception {
        Cuenta cuenta= cuentaServicio.getCuentaByCedula(cedula);
        return ResponseEntity.ok(new MensajeDTO<>(false,cuenta));

    }
}
