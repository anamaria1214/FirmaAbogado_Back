package Proyecto.servicios.implementaciones;

import Proyecto.dtos.factura.ActualizarFacturaDTO;
import Proyecto.dtos.factura.AgregarAbonoDTO;
import Proyecto.dtos.factura.CrearFacturaDTO;
import Proyecto.dtos.factura.ObtenerAbonoDTO;
import Proyecto.modelo.documentos.Factura;
import Proyecto.modelo.enums.EstadoFactura;
import Proyecto.modelo.documentos.Abono;
import Proyecto.modelo.vo.Pago;
import Proyecto.repositorios.AbonoRepo;
import Proyecto.repositorios.FacturaRepo;
import Proyecto.servicios.interfaces.FacturaServicio;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class FacturaServicioImpl implements FacturaServicio {

    private final FacturaRepo facturaRepo;
    private final AbonoRepo abonoRepo;

    public FacturaServicioImpl(FacturaRepo facturaRepo, AbonoRepo abonoRepo) {
        this.facturaRepo = facturaRepo;
        this.abonoRepo = abonoRepo;
    }


    /**
     * Crea una nueva factura asociada a un caso.
     *
     * @param crearFacturaDTO DTO que contiene los datos necesarios para crear la factura.
     * @throws Exception si ya existe una factura para el caso especificado.
     */
    @Override
    public void crearFactura(CrearFacturaDTO crearFacturaDTO) throws Exception {
        Optional<Factura> facturaOptional= facturaRepo.findByCaso(crearFacturaDTO.idCaso());
        if(facturaOptional.isPresent()){
            throw new Exception("Ya hay una factura creada para el caso");
        }
        List<String> abonos = new ArrayList<>();
        Factura factura = new Factura();
        factura.setConcepto(crearFacturaDTO.concepto());
        factura.setDescripcion(crearFacturaDTO.descripcion());
        factura.setEstadoFactura(EstadoFactura.PENDIENTE);
        factura.setValor(crearFacturaDTO.valor());
        factura.setIdCaso(crearFacturaDTO.idCaso());
        factura.setSaldoPendiente(crearFacturaDTO.valor());
        factura.setFecha(LocalDateTime.now());
        factura.setAbonos(abonos);

        facturaRepo.save(factura);
    }

    /**
     * Actualiza los datos de una factura existente, incluyendo su saldo pendiente.
     *
     * @param actualizarFacturaDTO DTO con los nuevos datos de la factura.
     * @throws Exception si la factura no existe.
     */
    @Override
    public void actualizarFactura(ActualizarFacturaDTO actualizarFacturaDTO) throws Exception {
        Factura factura = getFacturaById(actualizarFacturaDTO.idFactura());
        float nuevoSaldo=0;
        if(factura.getValor()>actualizarFacturaDTO.valor()){
            nuevoSaldo=factura.getSaldoPendiente()-(factura.getValor()-actualizarFacturaDTO.valor());
        }else{
            nuevoSaldo=factura.getSaldoPendiente()+(actualizarFacturaDTO.valor()-factura.getValor());
        }
        factura.setConcepto(actualizarFacturaDTO.concepto());
        factura.setDescripcion(actualizarFacturaDTO.descripcion());
        factura.setEstadoFactura(EstadoFactura.valueOf(actualizarFacturaDTO.estado()));
        factura.setValor(actualizarFacturaDTO.valor());
        factura.setSaldoPendiente(nuevoSaldo);
        facturaRepo.save(factura);
    }

    /**
     * Agrega un nuevo abono a una factura existente, actualizando su estado y registrando el abono.
     *
     * @param agregarAbonoDTO DTO con la información del abono.
     * @return ID del abono creado.
     * @throws Exception si el abono excede el saldo pendiente o si la factura no existe.
     */
    @Override
    public String agregarAbono(AgregarAbonoDTO agregarAbonoDTO) throws Exception {
        Factura factura = getFacturaById(agregarAbonoDTO.idFactura());

        if (factura.getSaldoPendiente() < agregarAbonoDTO.monto()) {
            throw new Exception("El valor a pagar excede el valor pendiente de la factura");
        }

        Abono abono = new Abono();
        abono.setMonto(agregarAbonoDTO.monto());
        abono.setFecha(LocalDateTime.now());
        Pago pago= new Pago();
        pago.setEstado("not approved");
        abono.setPago(pago);
        abonoRepo.save(abono);

        if (factura.getAbonos().isEmpty()) {
            factura.setEstadoFactura(EstadoFactura.PARCIAL);
        }

        factura.getAbonos().add(abono.getId());
        facturaRepo.save(factura);

        actualizarValorCaso(factura.getIdFactura());

        return abono.getId();
    }

    /**
     * Obtiene una factura asociada a un caso específico.
     *
     * @param idCaso ID del caso.
     * @return La factura correspondiente al caso.
     * @throws Exception si no existe una factura para ese caso.
     */
    @Override
    public Factura getFacturaByCaso(String idCaso) throws Exception {
        Optional<Factura> facturaOptional = facturaRepo.findByCaso(idCaso);
        if (facturaOptional.isEmpty()) {
            throw new Exception("No existe la factura");
        }
        Factura factura;
        factura = facturaOptional.get();
        return factura;
    }

    /**
     * Obtiene la lista de abonos asociados a una factura.
     *
     * @param idFactura ID de la factura.
     * @return Lista de abonos correspondientes a la factura.
     * @throws Exception si la factura o algún abono no existen.
     */
    @Override
    public List<Abono> getAbonosByFactura(String idFactura) throws Exception {
        Factura factura= getFacturaById(idFactura);
        List<Abono> abonos= new ArrayList<>();
        for(String idAbono: factura.getAbonos()){
            Optional<Abono> abono= abonoRepo.findById(idAbono);
            if(abono.isEmpty()){
                throw  new Exception("El abono no existe");
            }
            abonos.add(abono.get());
        }
        return abonos;
    }

    /**
     * Obtiene una factura a partir de su ID.
     *
     * @param idFactura ID de la factura.
     * @return Factura correspondiente al ID.
     * @throws Exception si la factura no existe.
     */
    @Override
    public Factura getFacturaById(String idFactura) throws Exception {
        Optional<Factura> facturaOptional = facturaRepo.findById(idFactura);
        if (facturaOptional.isEmpty()) {
            throw new Exception("No existe la factura");
        }
        Factura factura = new Factura();
        factura = facturaOptional.get();
        return factura;
    }
    /**
     * Genera una preferencia de pago en MercadoPago para un abono.
     *
     * @param idAbono ID del abono a pagar.
     * @return Objeto {@link Preference} generado por MercadoPago.
     * @throws Exception si el abono no existe.
     */
    @Override
    public Preference realizarPago(String idAbono) throws Exception {
        Abono abono = obtenerAbono(idAbono);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(abono.getId())
                        .quantity(1)
                        .currencyId("COP")
                        .unitPrice(BigDecimal.valueOf(abono.getMonto()))
                        .build();
        itemsPasarela.add(itemRequest);

        MercadoPagoConfig.setAccessToken("APP_USR-272595559782865-100719-958683f0949a50e7a8b92b77f7a696bf-2026139478");
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_abono", abono.getId()))
                .notificationUrl("https://firmaabogado-back-1.onrender.com/api/factura/mercadopago/notificacion")
                .build();
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        abono.setCodigoPasarela(preference.getId());
        abonoRepo.save(abono);

        return preference;
    }

    /**
     * Procesa la notificación enviada por MercadoPago al servidor cuando se actualiza el estado de un pago.
     *
     * @param request Mapa con los datos de la notificación.
     */
    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {
            Object tipo = request.get("type");

            if ("payment".equals(tipo)) {
                System.out.println(request);
                String input = request.get("data").toString();
                String idPago = input.replaceAll("\\D+", "");

                PaymentClient client = new PaymentClient();
                Payment payment = client.get( Long.parseLong(idPago) );

                String idAbono = payment.getMetadata().get("id_abono").toString();

                Abono abono = obtenerAbono(idAbono);
                Pago pago = crearPago(payment);
                abono.setPago(pago);
                abonoRepo.save(abono);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Obtiene un abono por su ID.
     *
     * @param idAbono ID del abono.
     * @return Abono correspondiente al ID.
     * @throws Exception si el abono no existe.
     */
    @Override
    public Abono obtenerAbono(String idAbono) throws Exception {
        Optional<Abono> abono= abonoRepo.findById(idAbono);
        if(abono.isEmpty()){
            throw new Exception("El abono no existe");
        }
        return abono.get();
    }

    /**
     * Recalcula el saldo pendiente de una factura en función de sus abonos aprobados.
     *
     * @param idFactura ID de la factura.
     * @throws Exception si la factura o algún abono no existen.
     */
    @Override
    public void actualizarValorCaso(String idFactura) throws Exception {
        Factura factura = getFacturaById(idFactura);

        float totalAbonado = 0;
        for (String idAbono : factura.getAbonos()) {
            Abono abono = obtenerAbono(idAbono);
            if (abono.getMonto() > 0 && abono.getPago().getEstado().equals("approved")) {
                totalAbonado += abono.getMonto();
            }
        }

        float saldoPendiente = factura.getValor() - totalAbonado;
        factura.setSaldoPendiente(saldoPendiente);

        if (saldoPendiente <= 0) {
            factura.setEstadoFactura(EstadoFactura.PAGADA);
        } else if (totalAbonado > 0) {
            factura.setEstadoFactura(EstadoFactura.PARCIAL);
        } else {
            factura.setEstadoFactura(EstadoFactura.PENDIENTE);
        }

        facturaRepo.save(factura);
    }

    /**
     * Crea un objeto {@link Pago} a partir de un objeto {@link Payment} de MercadoPago.
     *
     * @param payment Objeto de pago recibido desde MercadoPago.
     * @return Objeto {@link Pago} creado.
     */
    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setId(payment.getId().toString());
        pago.setFecha( payment.getDateCreated().toLocalDateTime() );
        pago.setEstado(payment.getStatus());
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(payment.getPaymentTypeId());
        pago.setMoneda(payment.getCurrencyId());
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(payment.getTransactionAmount().floatValue());
        return pago;
    }


}
