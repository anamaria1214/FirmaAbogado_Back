package Proyecto.servicios.interfaces;

import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Cuenta;

public interface CuentaServicio {

    TokenDTO login(LoginDTO loginDTO) throws Exception;

    Cuenta getCuentaByEmail(String email) throws Exception;

    void enviarCodigoRecuperacion(String correo) throws Exception;

    void cambioPassword(CambiarPasswordDTO cambiarPassword) throws Exception;

    CuentaDto actualizarCuenta (CuentaDto cuentaDto)throws Exception;

    InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception;

    CuentaDto crearCuenta(CuentaDto cuentaDto) throws Exception;
}
