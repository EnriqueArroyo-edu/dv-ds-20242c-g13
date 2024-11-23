package ar.edu.davinci.dv_ds_20242c_g13.strategy;

import java.math.BigDecimal;

public class PrendaNueva implements EstadoPrendaStrategy {
    @Override
    public BigDecimal obtenerPrecioVenta(BigDecimal precioBase) {
        return precioBase;
}
}