package Proyecto.servicios.interfaces;

import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.AgregarAbonoDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.dtos.factura.ObtenerAbonoDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.documentos.Abono;
import com.mercadopago.resources.preference.Preference;

import java.util.List;
import java.util.Map;


public interface FacturaServicio {

    void crearFactura(CrearFacturaDTO crearFacturaDTO) throws Exception;
    void actualizarFactura(ActualizarFacturaDTO actualizarFacturaDTO) throws Exception;
    void agregarAbono(AgregarAbonoDTO agregarAbonoDTO) throws Exception;
    Factura getFacturaByCaso(String idCaso) throws Exception;
    List<Abono> getAbonosByFactura(String idFactura) throws Exception;
    Factura getFacturaById(String idFactura) throws Exception;
    Preference realizarPago(String idAbono) throws Exception;
    void recibirNotificacionMercadoPago(Map<String, Object> request);
    Abono obtenerAbono(String idAbono) throws Exception;
    void actualizarValorCaso(String idFactura) throws Exception;

}
