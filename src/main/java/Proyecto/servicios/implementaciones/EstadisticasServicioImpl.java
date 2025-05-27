package Proyecto.servicios.implementaciones;
import Proyecto.modelo.documentos.Factura;
import Proyecto.repositorios.FacturaRepo;
import Proyecto.servicios.interfaces.EstadisticasServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstadisticasServicioImpl implements EstadisticasServicio {

    private FacturaRepo facturaRepo;


    /**
     * Calcula el porcentaje de facturas con estado PAGADA.
     */
    @Override
    public float porcentajeFacturasPagadas() {
        List<Factura> facturas= facturaRepo.findAll();
        int totalFacturas= facturas.size();
        int contador=0;
        for(Factura factura: facturas){
            if(factura.getEstadoFactura().name().equals("PAGADA")){
                contador++;
            }
        }
        return ((float) contador /totalFacturas)*100;
    }

    /**
     * Calcula el porcentaje de facturas con estado PARCIAL.
     */
    @Override
    public float porcentajeFacturasParciales() {
        List<Factura> facturas= facturaRepo.findAll();
        int totalFacturas= facturas.size();
        int contador=0;
        for(Factura factura: facturas){
            if(factura.getEstadoFactura().name().equals("PARCIAL")){
                contador++;
            }
        }
        return ((float) contador /totalFacturas)*100;
    }

    /**
     * Calcula el porcentaje de facturas con estado PENDIENTE.
     */
    @Override
    public float porcentajeFacturasPendientes() {
        List<Factura> facturas= facturaRepo.findAll();
        int totalFacturas= facturas.size();
        int contador=0;
        for(Factura factura: facturas){
            if(factura.getEstadoFactura().name().equals("PENDIENTE")){
                contador++;
            }
        }
        return ((float) contador /totalFacturas)*100;
    }


    /**
     * Retorna el valor total de todas las facturas.
     */
    @Override
    public float valorFacturas() {
        List<Factura> facturas= facturaRepo.findAll();
        float contador=0;
        for(Factura factura: facturas){
            contador+=factura.getValor();
        }
        return contador;
    }

    /**
     * Retorna el total de dinero ya recaudado (pagado).
     */
    @Override
    public float dineroRecaudado() {
        List<Factura> facturas= facturaRepo.findAll();
        float contador=0;
        for(Factura factura: facturas){
            contador+=factura.getValor()-factura.getSaldoPendiente();
        }
        return contador;
    }

    /**
     * Retorna el total de dinero restante por pagar.
     */
    @Override
    public float dineroRestante() {
        List<Factura> facturas= facturaRepo.findAll();
        float contador=0;
        for(Factura factura: facturas){
            contador+=factura.getSaldoPendiente();
        }
        return contador;
    }
}
