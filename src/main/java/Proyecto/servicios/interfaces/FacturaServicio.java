package Proyecto.servicios.interfaces;

import Proyecto.dtos.ActualizarCasoDTO;
import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.AgregarAbonoDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.vo.Abono;
import org.springframework.stereotype.Service;

import java.util.List;


public interface FacturaServicio {

    void crearFactura(CrearFacturaDTO crearFacturaDTO) throws Exception;
    void actualizarFactura(ActualizarFacturaDTO actualizarFacturaDTO) throws Exception;
    void agregarAbono(AgregarAbonoDTO agregarAbonoDTO) throws Exception;
    Factura getFacturaByCaso(String idCaso) throws Exception;
    List<Abono> getAbonosByFactura(String idFactura) throws Exception;
    Factura getFacturaById(String idFactura) throws Exception;

}
