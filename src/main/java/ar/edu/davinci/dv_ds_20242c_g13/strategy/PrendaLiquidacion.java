package ar.edu.davinci.dv_ds_20242c_g13.strategy;

import java.math.BigDecimal;

public class PrendaLiquidacion implements EstadoPrendaStrategy {

    private static final BigDecimal PORCENTAJE_DESCUENTO = new BigDecimal("0.50");

    @Override
    public BigDecimal obtenerPrecioVenta(BigDecimal precioBase) {
        return precioBase.multiply(PORCENTAJE_DESCUENTO).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}