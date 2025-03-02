package Proyecto.controllers;


import Proyecto.dtos.CuentaDto;
import Proyecto.servicios.interfaces.CuentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaViewController {
    @Autowired
    private CuentaServicio cuentaServicio;


    // Crear una nueva cuenta
    @PostMapping("/crear")
    public CuentaDto crearCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.crearCuenta(cuentaDto);
    }


    @PutMapping("/actualizar" )
    public CuentaDto actualizarCuenta(@RequestBody CuentaDto cuentaDto) throws Exception {
        return cuentaServicio.actualizarCuenta(cuentaDto);
    }
}
