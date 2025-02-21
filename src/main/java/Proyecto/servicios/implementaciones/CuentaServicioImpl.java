package Proyecto.servicios.implementaciones;

import Proyecto.dtos.InformacionCuentaDTO;
import Proyecto.dtos.RegistroDTO;
import Proyecto.modelo.documentos.Abogado;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.modelo.enums.TipoCuenta;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CuentaServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CuentaServicioImpl implements CuentaServicio {
    private final CuentaRepo cuentaRepo;

    public CuentaServicioImpl(CuentaRepo cuentaRepo) {
        this.cuentaRepo = cuentaRepo;
    }

    @Override
    public void crearCuenta(RegistroDTO infoCuenta) throws Exception {
        Cuenta cuenta= new Cuenta();
        cuenta.setEmail(infoCuenta.email());
        cuenta.setPassword(infoCuenta.password());
        if(infoCuenta.rol().equals("abogado")){
            cuenta.setTipoCuenta(TipoCuenta.ABOGADO);
        }else{
            cuenta.setTipoCuenta(TipoCuenta.CLIENTE);
        }
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuentaRepo.save(cuenta);

        if(cuenta.getTipoCuenta().equals(TipoCuenta.ABOGADO)){
            Abogado abogado= new Abogado();
            //Debo seguir creando el abogado y el cliente
        }
    }

    @Override
    public InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception {
        return null;
    }
}
