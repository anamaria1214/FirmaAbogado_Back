package Proyecto.servicios.implementaciones;

import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.AgregarAbonoDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.enums.EstadoFactura;
import Proyecto.modelo.vo.Abono;
import Proyecto.repositorios.FacturaRepo;
import Proyecto.servicios.interfaces.FacturaServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacturaServicioImpl implements FacturaServicio {

    private final FacturaRepo facturaRepo;

    public FacturaServicioImpl(FacturaRepo facturaRepo) {
        this.facturaRepo = facturaRepo;
    }

    @Override
    public void crearFactura(CrearFacturaDTO crearFacturaDTO) throws Exception {
        List<Abono> abonos= new ArrayList<>();
        Factura factura= new Factura();
        factura.setConcepto(crearFacturaDTO.concepto());
        factura.setDescripcion(crearFacturaDTO.descripcion());
        factura.setEstadoFactura(EstadoFactura.valueOf(crearFacturaDTO.estado()));
        factura.setValor(crearFacturaDTO.valor());
        factura.setIdCaso(crearFacturaDTO.idCaso());

        factura.setFecha(LocalDateTime.now());
        factura.setAbonos(abonos);

        facturaRepo.save(factura);
    }

    @Override
    public void actualizarFactura(ActualizarFacturaDTO actualizarFacturaDTO) throws Exception {
        Factura factura= getFacturaById(actualizarFacturaDTO.idFactura());

         factura.setConcepto(actualizarFacturaDTO.concepto());
         factura.setDescripcion(actualizarFacturaDTO.descripcion());
         factura.setEstadoFactura(EstadoFactura.valueOf(actualizarFacturaDTO.estado()));
         factura.setValor(actualizarFacturaDTO.valor());

         facturaRepo.save(factura);
    }

    @Override
    public void agregarAbono(AgregarAbonoDTO agregarAbonoDTO) throws Exception {

    }

    @Override
    public Factura getFacturaByCaso(String idCaso) throws Exception {
        Optional<Factura> facturaOptional= facturaRepo.findByCaso(idCaso);
        if(facturaOptional.isEmpty()){
            throw new Exception("No existe la factura");
        }
        Factura factura;
        factura= facturaOptional.get();
        return factura;
    }

    @Override
    public List<Abono> getAbonosByFactura(String idFactura) throws Exception {
        return null;
    }

    @Override
    public Factura getFacturaById(String idFactura) throws Exception {
        Optional<Factura> facturaOptional= facturaRepo.findById(idFactura);
        if(facturaOptional.isEmpty()){
            throw new Exception("No existe la factura");
        }
        Factura factura= new Factura();
        factura= facturaOptional.get();
        return factura;
    }
}
