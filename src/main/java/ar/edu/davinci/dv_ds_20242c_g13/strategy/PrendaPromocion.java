package ar.edu.davinci.dv_ds_20242c_g13.strategy;

import java.math.BigDecimal;

public class PrendaPromocion implements EstadoPrendaStrategy {

    private final BigDecimal descuento;

    public PrendaPromocion(BigDecimal descuento) {
        this.descuento = descuento;
    }

    @Override
    public BigDecimal obtenerPrecioVenta(BigDecimal precioBase) {
        if (descuento == null || descuento.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El descuento debe ser mayor o igual a 0.");
        }
        return precioBase.subtract(descuento).max(BigDecimal.ZERO); // Evita precios negativos
    }
}
