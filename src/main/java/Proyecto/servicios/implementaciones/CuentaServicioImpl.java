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

    /**
     * Construye un mapa de "claims" (información adicional) que se incluirá en el token JWT.
     * Estos claims permiten identificar el rol, nombre (email) e ID de la cuenta.
     *
     * @param cuenta La cuenta desde la cual se extrae la información.
     * @return Mapa con los claims del token.
     */
    private Map<String, Object> construirClaims(Cuenta cuenta) {
        return Map.of(
                "rol", cuenta.getTipoCuenta(),
                "nombre", cuenta.getEmail(),
                "id", cuenta.getIdCuenta()
        );
    }

    /**
     * Inicia sesión verificando las credenciales del usuario y genera un token JWT si son válidas.
     *
     * @param loginDTO Objeto que contiene el email y la contraseña proporcionados por el usuario.
     * @return TokenDTO que contiene el token JWT generado.
     * @throws Exception Si la cuenta no existe o la contraseña es incorrecta.
     */
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

    /**
     * Obtiene una cuenta por su correo electrónico.
     *
     * @param email Correo electrónico de la cuenta.
     * @return Objeto Cuenta correspondiente.
     * @throws Exception Si no se encuentra una cuenta con el correo proporcionado.
     */
    @Override
    public Cuenta getCuentaByEmail(String email) throws Exception {
        return cuentaRepo.findByEmail(email).orElseThrow(()->new Exception("La cuenta no existe"));
    }

    /**
     * Obtiene una cuenta a partir del ID (cédula) y la convierte en un DTO.
     *
     * @param id Identificador de la cuenta (cédula).
     * @return Objeto CuentaDto correspondiente a la cuenta encontrada.
     * @throws Exception Si no se encuentra ninguna cuenta con la cédula dada.
     */
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

    /**
     * Encripta una contraseña utilizando el algoritmo BCrypt.
     *
     * @param password La contraseña en texto plano a encriptar.
     * @return La contraseña encriptada con BCrypt.
     */
    public String encriptarPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Genera un código aleatorio de 6 caracteres que puede usarse para validación,
     * por ejemplo, en recuperación de contraseñas.
     *
     * @return Código aleatorio compuesto por letras y números.
     */
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

    /**
     * Envía un código de validación al correo del usuario para recuperación de contraseña.
     *
     * @param correo Dirección de correo del usuario.
     * @throws Exception Si no se encuentra una cuenta con el correo proporcionado.
     */
    @Override
    public void enviarCodigoRecuperacion(String correo) throws Exception{
        Cuenta cUsuario = getCuentaByEmail(correo);
        String codigoValidacion= generarCodigoValidacion();
        cUsuario.setCodValidacionPassword(new CodigoValidacion(LocalDateTime.now(), codigoValidacion));
        emailServicio.enviarCorreo(new EmailDTO("Código de validación", "El código de validación es: "+codigoValidacion+". Este código tiene una duración de 15 minutos, después de este tiempo, no será valido", correo) );
        cuentaRepo.save(cUsuario);
    }

    /**
     * Cambia la contraseña de una cuenta, validando el código enviado por correo.
     *
     * @param cambiarPassword DTO que contiene el email, el nuevo password, su repetición y el código de verificación.
     * @throws Exception Si la cuenta no existe, las contraseñas no coinciden, el código es incorrecto o ha expirado.
     */
    @Override
    public void cambioPassword(CambiarPasswordDTO cambiarPassword) throws Exception {
        System.out.println(cambiarPassword);
        Cuenta cUsuario = getCuentaByEmail(cambiarPassword.email());
        if(cUsuario==null){
            throw new Exception("Cuenta no encontrada");
        }
        if(!cambiarPassword.passwordNueva().equals(cambiarPassword.repetirContraseña())){
            throw new Exception("Las contraseñan no coinciden");
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

    /**
     * Elimina una cuenta del sistema a partir de los datos proporcionados en el DTO.
     *
     * @param cuentaDto DTO que contiene los datos de la cuenta a eliminar, como la cédula.
     * @throws RuntimeException Si no se encuentra ninguna cuenta con la cédula proporcionada.
     */
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

    /**
     * Actualiza los datos de una cuenta existente.
     *
     * @param cuentaDto DTO con los nuevos datos de la cuenta.
     * @return El DTO de la cuenta actualizada.
     * @throws Exception Si no se encuentra la cuenta por el email.
     */
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


    /**
     * Crea una nueva cuenta de usuario en el sistema.
     *
     * @param cuentaDto DTO con los datos de la nueva cuenta.
     * @return DTO de la cuenta creada.
     * @throws Exception Si ya existe una cuenta con el mismo correo electrónico.
     */
    @Override
    public CuentaDto crearCuenta(CuentaDto cuentaDto) throws Exception {

        if(cuentaRepo.findByEmail(cuentaDto.getEmail()).isPresent()){
            throw new Exception("Ya existe una cuenta con este email");
        }
        // Convertir de DTO a entidad
        Cuenta cuenta = toEntity(cuentaDto);

        // Encriptar la contraseña antes de guardarla
        cuenta.setPassword(encriptarPassword(cuentaDto.getPassword()));

        // Guardar en la base de datos
        Cuenta cuentaGuardada = cuentaRepo.save(cuenta);

        // Retornar la cuenta creada como DTO
        return toDto(cuentaGuardada);
    }

    /**
     * Obtiene una cuenta a partir de su cédula.
     *
     * @param cedula Cédula de la cuenta.
     * @return La entidad Cuenta correspondiente.
     * @throws Exception Si no se encuentra la cuenta.
     */
    @Override
    public Cuenta getCuentaByCedula(String cedula) throws Exception {
        return cuentaRepo.findByCedula(cedula).orElseThrow(()->new Exception("La cuenta no existe"));
    }

    /**
     * Crea una cuenta con el rol de ABOGADO a partir de un DTO especializado.
     *
     * @param cuentaAbogadoDTO DTO con los datos del abogado.
     * @throws Exception Si ya existe una cuenta con el mismo correo.
     */
    @Override
    public void crearCuentaAbogado(CuentaAbogadoDTO cuentaAbogadoDTO) throws Exception {
        if(getCuentaByEmail(cuentaAbogadoDTO.email())==null){
            throw new Exception("La cuenta con este correo ya existe");
        }
        Cuenta cuenta= new Cuenta();
        cuenta.setCedula(cuentaAbogadoDTO.cedula());
        cuenta.setEspecializaciones(cuentaAbogadoDTO.especializaciones());
        cuenta.setNombre(cuentaAbogadoDTO.nombre());
        cuenta.setTelefono(cuentaAbogadoDTO.telefono());
        cuenta.setEmail(cuentaAbogadoDTO.email());
        cuenta.setPassword(encriptarPassword(cuentaAbogadoDTO.password()));
        cuenta.setDireccion(cuentaAbogadoDTO.direccion());
        cuenta.setFechaCreacion(cuentaAbogadoDTO.fechaCreacion());
        cuenta.setTipoCuenta(TipoCuenta.ABOGADO);

        cuentaRepo.save(cuenta);
    }

    /**
     * Retorna la información completa de una cuenta (placeholder para implementación).
     *
     * @param id ID de la cuenta.
     * @return Un DTO con la información de la cuenta.
     * @throws Exception En caso de error.
     */
    @Override
    public InformacionCuentaDTO obtenerInfoCuenta(String id) throws Exception {
        return null;
    }

    /**
     * Convierte una entidad Cuenta a su correspondiente DTO.
     *
     * @param cuenta Entidad Cuenta.
     * @return DTO de la cuenta.
     */
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

    /**
     * Convierte un DTO de cuenta a su entidad correspondiente.
     *
     * @param cuentaDto DTO de cuenta.
     * @return Entidad Cuenta.
     */
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
