import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import Proyecto.dtos.ActualizarCasoDTO;
import Proyecto.dtos.ComentarCasoDTO;
import Proyecto.dtos.CorreoCasoDTO;
import Proyecto.dtos.CrearCasoDTO;
import Proyecto.modelo.documentos.Caso;
import Proyecto.modelo.documentos.Cuenta;
import Proyecto.repositorios.CasoRepo;
import Proyecto.repositorios.CuentaRepo;
import Proyecto.servicios.implementaciones.CasoServicioImpl;
import Proyecto.servicios.interfaces.CuentaServicio;
import Proyecto.servicios.interfaces.EmailServicio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CasoServicioImplTest {

    @Mock
    private CasoRepo casoRepo;

    @Mock
    private CuentaRepo clienteRepo;
    @Mock
    private EmailServicio emailServicio;

    @Mock
    private CuentaServicio cuentaServicio;

    @InjectMocks
    private CasoServicioImpl casoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarCasosClientes_clienteNoExiste() {
        when(clienteRepo.findByCedula("123456789")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.listarCasosClientes("123456789"));
        assertEquals("Cliente no existe", ex.getMessage());
    }

    @Test
    void testListarCasosClientes_exito() throws Exception {
        when(clienteRepo.findByCedula("123456789")).thenReturn(Optional.of(new Cuenta()));
        List<Caso> casos = List.of(new Caso());
        when(casoRepo.buscarCasosPorClientes("123456789")).thenReturn(casos);
        List<Caso> resultado = casoService.listarCasosClientes("123456789");
        assertEquals(1, resultado.size());
    }

    @Test
    void testListarCasosAbogados_abogadoNoExiste() {
        when(clienteRepo.findByCedula("1094889063")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.listarCasosAbogados("123456789"));
        assertEquals("Cliente no existe", ex.getMessage());
    }

    @Test
    void testListarCasosAbogados_exito() throws Exception {
        when(clienteRepo.findByCedula("1094889063")).thenReturn(Optional.of(new Cuenta()));
        List<Caso> casos = List.of(new Caso());
        when(casoRepo.buscarCasosPorAbogado("123456789")).thenReturn(casos);
        List<Caso> resultado = casoService.listarCasosAbogados("123456789");
        assertEquals(1, resultado.size());
    }

    @Test
    void testCrearCaso_camposObligatoriosVacios() {
        CrearCasoDTO dto = new CrearCasoDTO("", "", LocalDateTime.now(), null, null, "ACTIVO");
        Exception ex = assertThrows(Exception.class, () -> casoService.crearCaso(dto));
        assertEquals("Campos obligatorios vacios", ex.getMessage());
    }

    @Test
    void testActualizarCaso_casoNoEncontrado() {
        ArrayList<String> clientes= new ArrayList<>();
        clientes.add("1094889063");
        ArrayList<String> abogados= new ArrayList<>();
        abogados.add("123456789");
        when(casoRepo.findById("id123")).thenReturn(Optional.empty());
        ActualizarCasoDTO dto = new ActualizarCasoDTO("68057871ec5734127186e111", "Violencia sexual a menor de 16 años", "Es acusado de violencia sexual a menor de 14 años hace más de 10 años el 10 de octubre de 2015, pero el es inocente", "ACTIVO", clientes, abogados);
        Exception ex = assertThrows(Exception.class, () -> casoService.actualizarCaso(dto));
        assertEquals("Caso no encontrado", ex.getMessage());
    }


    @Test
    void testNotificarCambios_casoNoEncontrado() {
        when(casoRepo.findById("idCaso")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.notificarCambios("idCaso"));
        assertEquals("Caso no encontrado", ex.getMessage());
    }

    @Test
    void testDevolverTodosCasos_exito() throws Exception {
        when(casoRepo.findAll()).thenReturn(List.of(new Caso()));
        List<Caso> resultado = casoService.devolverTodosCasos();
        assertEquals(1, resultado.size());
    }

    @Test
    void testListarComentarios_casoNoExiste() {
        when(casoRepo.findById("idCaso")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.listarComentarios("idCaso"));
        assertEquals("No existe el caso", ex.getMessage());
    }

    @Test
    void testEnviarCorreoSobreCaso_casoNoExiste() {
        CorreoCasoDTO dto = new CorreoCasoDTO("idCaso", "Asunto", "Cuerpo");
        when(casoRepo.findById("idCaso")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.enviarCorreoSobreCaso(dto));
        assertEquals("Caso no existente", ex.getMessage());
    }

    @Test
    void testComentarCaso_camposVacios() {
        ComentarCasoDTO dto = new ComentarCasoDTO("68057871ec5734127186e111", "", "", "6805089f90298a4a27e5a68da");
        Exception ex = assertThrows(Exception.class, () -> casoService.comentarCaso(dto));
        assertEquals("Campos obligatorios vacios", ex.getMessage());
    }

    @Test
    void testComentarCaso_casoNoEncontrado() {
        ComentarCasoDTO dto = new ComentarCasoDTO("idCaso", "Asunto", "Descripcion", "6805089f90298a4a27e5a68d");
        when(casoRepo.findById("idCaso")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> casoService.comentarCaso(dto));
        assertEquals("Caso no encontrado.", ex.getMessage());
    }

}
