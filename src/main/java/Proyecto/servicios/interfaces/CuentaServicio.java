package Proyecto.servicios.interfaces;

import Proyecto.dtos.InformacionCuentaDTO;
import Proyecto.dtos.RegistroDTO;
import Proyecto.modelo.documentos.Cuenta;

public interface CuentaServicio {

    void crearCuenta(RegistroDTO infoCuenta) throws Exception;

    InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception;
}
