package Proyecto.controllers;


import Proyecto.dtos.*;

import Proyecto.dtos.caso.InfoCasosDTO;
import Proyecto.dtos.cuenta.CuentaAbogadoDTO;
import Proyecto.dtos.cuenta.CuentaDto;
import Proyecto.modelo.documentos.Cuenta;

import Proyecto.servicios.interfaces.CasoServicio;

import Proyecto.servicios.interfaces.CuentaServicio;
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


    // Crear una nueva cuenta
    @PostMapping("/crear")
    public CuentaDto crearCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.crearCuenta(cuentaDto);
    }

    @PutMapping("/actualizar" )
    public CuentaDto actualizarCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.actualizarCuenta(cuentaDto);
    }

    // Eliminar un admin por ID: DELETE /api/admin/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) throws Exception {
        CuentaDto clientDelete = cuentaServicio.getCuentaById(id);
        cuentaServicio.eliminarCuenta(clientDelete);
    }
    @GetMapping("/listarCasos/{id}")
    public ResponseEntity<MensajeDTO<List<InfoCasosDTO>>> listarCasos(@PathVariable("id")String idCliente) throws Exception {
        List<InfoCasosDTO> casosCliente= casoServicio.listarCasosClientes(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false,casosCliente));

    }
    @PostMapping("/crearAbogado")
    public ResponseEntity<MensajeDTO<String>> crearCuentaAbogado(@RequestBody CuentaAbogadoDTO cuentaAbogadoDTO) throws Exception {
        cuentaServicio.crearCuentaAbogado(cuentaAbogadoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false,"Abogado creado correctamente"));
    }

    @GetMapping("/getCuentaByCedula/{cedula}")
    public ResponseEntity<MensajeDTO<Cuenta>> getCuentaByCedula(@PathVariable("cedula")String cedula) throws Exception {
        Cuenta cuenta= cuentaServicio.getCuentaByCedula(cedula);
        return ResponseEntity.ok(new MensajeDTO<>(false,cuenta));

    }
}
