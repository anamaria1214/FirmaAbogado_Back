package Proyecto.servicios.implementaciones;

import Proyecto.config.JWTUtils;
import Proyecto.dtos.*;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.modelo.enums.TipoCuenta;
import Proyecto.modelo.vo.CodigoValidacion;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EmailServicio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
@Service
@Transactional
public class CuentaServicioImpl implements CuentaServicio {
    private final CuentaRepo cuentaRepo;
    private final EmailServicio emailServicio;
    private final JWTUtils jwtUtils;

    public CuentaServicioImpl(CuentaRepo cuentaRepo, EmailServicio emailServicio, JWTUtils jwtUtils) {
        this.cuentaRepo = cuentaRepo;
        this.emailServicio = emailServicio;
        this.jwtUtils = jwtUtils;
    }

    private Map<String, Object> construirClaims(Cuenta cuenta) {
        return Map.of(
                "rol", cuenta.getTipoCuenta(),
                "nombre", cuenta.getEmail(),
                "id", cuenta.getIdCuenta()
        );
    }
    @Override
    public TokenDTO login(LoginDTO loginDTO) throws Exception {
        Cuenta cuenta = getCuentaByEmail(loginDTO.email());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(loginDTO.password(), cuenta.getPassword()) ) {
            throw new Exception("La contraseña es incorrecta");
        }
        Map<String, Object> map = construirClaims(cuenta);
        return new TokenDTO(jwtUtils.generarToken(cuenta.getEmail(), map) );
    }

    @Override
    public Cuenta getCuentaByEmail(String email) throws Exception {
        return cuentaRepo.findByEmail(email).orElseThrow(()->new Exception("La cuenta no existe"));
    }

    @Override
    public CuentaDto getCuentaById(String id) throws Exception {
        // Buscar la cuenta por su ID en la base de datos
        Optional<Cuenta> cuentaOptional = cuentaRepo.findByCedula(id);

        // Verificar si la cuenta existe
        if (cuentaOptional.isPresent()) {
            return toDto(cuentaOptional.get());  // Convertir la entidad a DTO y retornar
        } else {
            throw new RuntimeException("Cuenta no encontrada con cédula: " + id);
        }
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
        System.out.println(cambiarPassword);
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

    @Override
    public void eliminarCuenta(CuentaDto cuentaDto) {

            // Verificar si la cuenta existe antes de eliminarla
            Optional<Cuenta> cuentaOptional = cuentaRepo.findByCedula(cuentaDto.getCedula());
            if (cuentaOptional.isEmpty()) {
                throw new RuntimeException("La cedula " + cuentaDto.getCedula() + " no se encuentra registrada.");
            }

            // Si existe, eliminar la cuenta
            cuentaRepo.delete(cuentaOptional.get());
        }


    @Override
    public CuentaDto actualizarCuenta(CuentaDto cuentaDto) throws Exception {
        // Buscar la cuenta por el email
        Cuenta cuenta = cuentaRepo.findByEmail(cuentaDto.getEmail())
                .orElseThrow(() -> new Exception("Cuenta no encontrada"));

        // Actualizar los datos de la cuenta
        cuenta.setNombre(cuentaDto.getNombre());
        cuenta.setTelefono(cuentaDto.getTelefono());
        cuenta.setDireccion(cuentaDto.getDireccion());

        // Guardar los cambios en la base de datos
        cuentaRepo.save(cuenta);

        // Retornar un nuevo CuentaDto con la información actualizada
        return toDto(cuenta);
    }


    @Override
    public InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception {
        return null;
    }

    @Override
    public CuentaDto crearCuenta(CuentaDto cuentaDto) throws Exception {
        // Convertir de DTO a entidad
        Cuenta cuenta = toEntity(cuentaDto);

        // Encriptar la contraseña antes de guardarla
        cuenta.setPassword(encriptarPassword(cuentaDto.getPassword()));

        // Guardar en la base de datos
        Cuenta cuentaGuardada = cuentaRepo.save(cuenta);

        // Retornar la cuenta creada como DTO
        return toDto(cuentaGuardada);
    }

    private CuentaDto toDto(Cuenta cuenta) {
        return new CuentaDto(
                cuenta.getCedula(),
                cuenta.getNombre(),
                cuenta.getTelefono(),
                cuenta.getEmail(),
                cuenta.getDireccion(),
        null,
                cuenta.getTipoCuenta().name() // Convertimos el enum a String
        );
    }

    private Cuenta toEntity(CuentaDto cuentaDto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setCedula(cuentaDto.getCedula());
        cuenta.setNombre(cuentaDto.getNombre());
        cuenta.setTelefono(cuentaDto.getTelefono());
        cuenta.setEmail(cuentaDto.getEmail());
        cuenta.setPassword(cuentaDto.getPassword());
        cuenta.setDireccion(cuentaDto.getDireccion());
        cuenta.setFechaCreacion(LocalDateTime.now());

        // Asignar tipo de cuenta basado en el DTO
        if ("abogado".equalsIgnoreCase(cuentaDto.getRol())) {
            cuenta.setTipoCuenta(TipoCuenta.ABOGADO);
        } else if ("admin".equalsIgnoreCase(cuentaDto.getRol())) {
            cuenta.setTipoCuenta(TipoCuenta.ADMIN);
        } else {
            cuenta.setTipoCuenta(TipoCuenta.CLIENTE);
        }

        return cuenta;
    }

}
