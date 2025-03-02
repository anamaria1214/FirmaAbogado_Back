package Proyecto.servicios.interfaces;

import Proyecto.dtos.CambiarPasswordDTO;
import Proyecto.dtos.CuentaDto;
import Proyecto.dtos.InformacionCuentaDTO;
import Proyecto.dtos.RegistroDTO;
import Proyecto.modelo.documentos.Cuenta;

public interface CuentaServicio {

    Cuenta getCuentaByEmail(String email) throws Exception;

    void enviarCodigoRecuperacion(String correo) throws Exception;

    void cambioPassword(CambiarPasswordDTO cambiarPassword) throws Exception;

    CuentaDto actualizarCuenta (CuentaDto cuentaDto)throws Exception;

    InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception;

    CuentaDto crearCuenta(CuentaDto cuentaDto) throws Exception;
}
