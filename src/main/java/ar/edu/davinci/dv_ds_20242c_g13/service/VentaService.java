package ar.edu.davinci.dv_ds_20242c_g13.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Venta;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;

public interface VentaService {
    // Métodos de creación de una venta en Efectivo
    VentaEfectivo save(VentaEfectivo venta) throws BusinessException;
    VentaEfectivo save(VentaEfectivo venta, Item item) throws BusinessException;

    // Métodos de creación de una venta en Tarjeta
    VentaTarjeta save(VentaTarjeta venta) throws BusinessException;
    VentaTarjeta save(VentaTarjeta venta, Item item) throws BusinessException;
    
    
    // metodo generico
    Venta save(Venta venta) throws BusinessException;


    // Eliminación de una venta
    void delete(Venta venta);
    void delete(Long id);

    // Método de búsqueda por ID
    Venta findById(Long id) throws BusinessException;

    // Métodos de listado
    List<Venta> list();
    Page<Venta> list(Pageable pageable);

    // Contar cantidad de ventas
    long count();

    // Métodos para gestionar items de una venta
    Venta addItem(Long ventaId, Item item) throws BusinessException;
    Venta updateItem(Long ventaId, Long itemId, Item item) throws BusinessException;
    Venta deleteItem(Long ventaId, Long itemId) throws BusinessException;

    // Métodos nuevos
    List<Venta> obtenerVentasPorFecha(LocalDate fecha);
    BigDecimal calcularTotalPorFormaDePago(Class<? extends Venta> tipoVenta);
}
