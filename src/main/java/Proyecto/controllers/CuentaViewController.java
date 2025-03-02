package Proyecto.controllers;


import Proyecto.dtos.CambiarPasswordDTO;
import Proyecto.dtos.CuentaDto;
import Proyecto.dtos.InfoCasosDTO;
import Proyecto.dtos.MensajeDTO;
import Proyecto.servicios.interfaces.CasoServicio;
import Proyecto.servicios.interfaces.CuentaServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaViewController {
    @Autowired
    private CuentaServicio cuentaServicio;
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

    @GetMapping("/listarCasos/{id}")
    public ResponseEntity<MensajeDTO<List<InfoCasosDTO>>> listarCasos(@PathVariable("id")String idCliente) throws Exception {
        List<InfoCasosDTO> casosCliente= casoServicio.listarCasosClientes(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false,casosCliente));
    }
}
