package ar.edu.davinci.dv_ds_20242c_g13.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Cliente;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Venta;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20242c_g13.repository.VentaEfectivoRepository;
import ar.edu.davinci.dv_ds_20242c_g13.repository.VentaRepository;
import ar.edu.davinci.dv_ds_20242c_g13.repository.VentaTarjetaRepository;
@Service
public class VentaServiceImpl implements VentaService {
    private final Logger LOGGER = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final VentaEfectivoRepository ventaEfectivoRepository;
    private final VentaTarjetaRepository ventaTarjetaRepository;
    private final ClienteService clienteService;
    private final PrendaService prendaService;
    private final ItemService itemService;

    @Autowired
    public VentaServiceImpl(final VentaRepository ventaRepository,
                            final VentaEfectivoRepository ventaEfectivoRepository,
                            final VentaTarjetaRepository ventaTarjetaRepository,
                            final ClienteService clienteService,
                            final PrendaService prendaService,
                            final ItemService itemService) {
        this.ventaRepository = ventaRepository;
        this.ventaEfectivoRepository = ventaEfectivoRepository;
        this.ventaTarjetaRepository = ventaTarjetaRepository;
        this.clienteService = clienteService;
        this.prendaService = prendaService;
        this.itemService = itemService;
    }

    @Override
    public VentaEfectivo save(VentaEfectivo venta) throws BusinessException {
        Cliente cliente = getCliente(venta.getCliente().getId());
        List<Item> items = venta.getItems() != null ? getItems(venta.getItems()) : new ArrayList<>();

        venta = VentaEfectivo.builder()
                .cliente(cliente)
                .fecha(Calendar.getInstance().getTime())
                .items(items)
                .build();
        return ventaEfectivoRepository.save(venta);
    }

    @Override
    public VentaEfectivo save(VentaEfectivo ventaEfectivo, Item item) throws BusinessException {
        ventaEfectivo.addItem(item);
        return ventaEfectivoRepository.save(ventaEfectivo);
    }

    @Override
    public VentaTarjeta save(VentaTarjeta venta) throws BusinessException {
        Cliente cliente = getCliente(venta.getCliente().getId());
        List<Item> items = venta.getItems() != null ? getItems(venta.getItems()) : new ArrayList<>();

        venta = VentaTarjeta.builder()
                .cliente(cliente)
                .fecha(Calendar.getInstance().getTime())
                .items(items)
                .cantidadCuotas(venta.getCantidadCuotas())
                .coeficienteTarjeta(new BigDecimal(0.01))
                .build();
        return ventaTarjetaRepository.save(venta);
    }

    @Override
    public VentaTarjeta save(VentaTarjeta ventaTarjeta, Item item) throws BusinessException {
        ventaTarjeta.addItem(item);
        return ventaTarjetaRepository.save(ventaTarjeta);
    }

    @Override
    public void delete(Venta venta) {
        ventaRepository.delete(venta);
    }

    @Override
    public void delete(Long id) {
        ventaRepository.deleteById(id);
    }

    @Override
    public Venta findById(Long id) throws BusinessException {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("No se encontró la venta por el id: " + id));
    }

    @Override
    public List<Venta> list() {
        LOGGER.debug("Listado de todas las ventas");
        return ventaRepository.findAll();
    }

    @Override
    public Page<Venta> list(Pageable pageable) {
        LOGGER.debug("Listado de todas las ventas por páginas");
        return ventaRepository.findAll(pageable);
    }

    @Override
    public long count() {
        return ventaRepository.count();
    }

    @Override
    public Venta addItem(Long ventaId, Item item) throws BusinessException {
        Venta venta = getVenta(ventaId);
        Prenda prenda = getPrenda(item);
        Item newItem = Item.builder()
                .cantidad(item.getCantidad())
                .prenda(prenda)
                .venta(venta)
                .build();
        newItem = itemService.save(newItem);
        venta.addItem(newItem);
        return venta;
    }

    @Override
    public Venta updateItem(Long ventaId, Long itemId, Item item) throws BusinessException {
        Item actualItem = getItem(itemId);
        actualItem.setCantidad(item.getCantidad());
        itemService.update(actualItem);
        return getVenta(ventaId);
    }

    @Override
    public Venta deleteItem(Long ventaId, Long itemId) throws BusinessException {
        Item actualItem = getItem(itemId);
        itemService.delete(itemId);
        Venta venta = getVenta(ventaId);
        venta.getItems().remove(actualItem);
        return ventaRepository.save(venta);
    }

    
    @Override
    public List<Venta> obtenerVentasPorFecha(LocalDate fecha) {
        return ventaRepository.findAll().stream()
                .filter(venta -> venta.esDeFecha(java.sql.Date.valueOf(fecha)))
                .toList();
    }


    @Override
    public BigDecimal calcularTotalPorFormaDePago(Class<? extends Venta> tipoVenta) {
        return ventaRepository.findAll().stream()
                .filter(tipoVenta::isInstance)
                .map(Venta::importeFinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Métodos privados auxiliares
    private Venta getVenta(Long ventaId) throws BusinessException {
        return ventaRepository.findById(ventaId)
                .orElseThrow(() -> new BusinessException("Venta no encontrada para el id: " + ventaId));
    }

    private List<Item> getItems(List<Item> requestItems) throws BusinessException {
        List<Item> items = new ArrayList<>();
        for (Item requestItem : requestItems) {
            Prenda prenda = getPrenda(requestItem);
            items.add(Item.builder()
                    .cantidad(requestItem.getCantidad())
                    .prenda(prenda)
                    .build());
        }
        return items;
    }

    private Prenda getPrenda(Item requestItem) throws BusinessException {
        return Optional.ofNullable(requestItem.getPrenda().getId())
                .map(id -> {
                    try {
                        return prendaService.findById(id);
                    } catch (BusinessException e) {
                        throw new RuntimeException("Error al buscar la prenda", e);
                    }
                })
                .orElseThrow(() -> new BusinessException("La Prenda es obligatoria"));
    }
    private Item getItem(Long id) throws BusinessException {
        return itemService.findById(id);
    }

    private Cliente getCliente(Long id) throws BusinessException {
        return clienteService.findById(id);
    }
    
    @Override
    public Venta save(Venta venta) throws BusinessException {
        if (venta instanceof VentaEfectivo) {
            return save((VentaEfectivo) venta);
        } else if (venta instanceof VentaTarjeta) {
            return save((VentaTarjeta) venta);
        } else {
            throw new BusinessException("Tipo de venta no soportado: " + venta.getClass().getSimpleName());
        }
    }

}
