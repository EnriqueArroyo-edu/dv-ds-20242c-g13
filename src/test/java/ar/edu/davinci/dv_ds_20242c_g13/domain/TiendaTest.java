package ar.edu.davinci.dv_ds_20242c_g13.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Tienda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaEfectivo;

public class TiendaTest {

    @Test
    public void testCalcularGananciasDia() {
        // Arrange
        Tienda tienda = new Tienda();
        tienda.setNombre("Tienda de Prueba");

        List<VentaEfectivo> ventas = new ArrayList<>();

        LocalDate fechaPrueba = LocalDate.of(2024, 11, 20);
        Date fechaVenta = Date.from(fechaPrueba.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Crear Prenda 1
        Prenda prenda1 = Prenda.builder()
                .descripcion("Camisa")
                .precioBase(new BigDecimal("100.00"))
                .build();

        // Crear Prenda 2
        Prenda prenda2 = Prenda.builder()
                .descripcion("Pantalón")
                .precioBase(new BigDecimal("200.00"))
                .build();

        // Crear venta 1 con items
        VentaEfectivo venta1 = new VentaEfectivo();
        venta1.setFecha(fechaVenta);

        Item item1 = new Item();
        item1.setCantidad(1);
        item1.setPrenda(prenda1);
        venta1.addItem(item1);

        // Crear venta 2 con items
        VentaEfectivo venta2 = new VentaEfectivo();
        venta2.setFecha(fechaVenta);

        Item item2 = new Item();
        item2.setCantidad(1);
        item2.setPrenda(prenda2);
        venta2.addItem(item2);

        ventas.add(venta1);
        ventas.add(venta2);

        tienda.setVentas((List) ventas); // Convertir a List<Venta>

        // Act
        BigDecimal ganancias = tienda.calcularGananciasDia(fechaPrueba);

        // Assert
        assertNotNull(ganancias, "Las ganancias no deberían ser nulas");
        assertEquals(new BigDecimal("300.00"), ganancias, "Las ganancias deberían ser 300.00");
    }
}
