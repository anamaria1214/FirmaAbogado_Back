package Proyecto.servicios.implementaciones;

import Proyecto.dtos.CambiarPasswordDTO;
import Proyecto.dtos.EmailDTO;
import Proyecto.dtos.InformacionCuentaDTO;
import Proyecto.dtos.RegistroDTO;
import Proyecto.modelo.documentos.Abogado;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.modelo.enums.TipoCuenta;
import Proyecto.modelo.vo.CodigoValidacion;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EmailServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CuentaServicioImpl implements CuentaServicio {
    private final CuentaRepo cuentaRepo;
    private final EmailServicio emailServicio;

    public CuentaServicioImpl(CuentaRepo cuentaRepo, EmailServicio emailServicio) {
        this.cuentaRepo = cuentaRepo;
        this.emailServicio = emailServicio;
    }

    @Override
    public Cuenta getCuentaByEmail(String email) throws Exception {
        return cuentaRepo.buscarEmail(email).orElseThrow(()->new Exception("La cuenta no existe"));
    }

    public String encriptarPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private String generarCodigoValidacion(){

        String cadena ="ABCDEFGHIJKMNÑOPQRSTUVWXYZ1234567890";
        String resul="";

        for(int i=0; i<6;i++){
            int indice = (int) (Math.random()*cadena.length());
            char car= cadena.charAt(indice);
            resul+=car;
        }
        return resul;
    }
    @Override
    public void enviarCodigoRecuperacion(String correo) throws Exception{
        Cuenta cUsuario = getCuentaByEmail(correo);
        String codigoValidacion= generarCodigoValidacion();
        cUsuario.setCodValidacionPassword(new CodigoValidacion(LocalDateTime.now(), codigoValidacion));
        emailServicio.enviarCorreo( new EmailDTO("Código de validación", "El código de validación es: "+codigoValidacion+". Este código tiene una duración de 15 minutos, después de este tiempo, no será valido", correo) );
        cuentaRepo.save(cUsuario);
    }
    @Override
    public void cambioPassword(CambiarPasswordDTO cambiarPassword) throws Exception {
        Cuenta cUsuario = getCuentaByEmail(cambiarPassword.email());
        if(cUsuario==null){
            throw new Exception("Cuenta no encontrada");
        }
        CodigoValidacion codigoValidacion= cUsuario.getCodValidacionPassword();
        if(codigoValidacion!= null){
            String codigo= codigoValidacion.getCodigo();
            if(codigo.equals(cambiarPassword.codigoVerificacion())){
                if(codigoValidacion.getFechaCreacion().plusMinutes(15).isAfter(LocalDateTime.now())){
                    cUsuario.setPassword(encriptarPassword(cambiarPassword.passwordNueva()));
                    cuentaRepo.save(cUsuario);
                }else{
                    throw  new Exception("Su código de verificación ya expiró");
                }
            }else{
                throw new Exception("El código es incorrecto");
            }
        }
    }

    //No terminado
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
