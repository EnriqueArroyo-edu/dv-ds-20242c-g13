package ar.edu.davinci.dv_ds_20242c_g13.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tienda {

    private String nombre;
    private List<Venta> ventas;

    /**
     * Calcula las ganancias de un día específico.
     * 
     * @param fecha Día para el cálculo.
     * @return Total de ganancias del día como BigDecimal.
     */
    public BigDecimal calcularGananciasDia(LocalDate fecha) {
        if (ventas == null || ventas.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return ventas.stream()
                .filter(venta -> venta.getFecha().toInstant()
                                      .atZone(ZoneId.systemDefault())
                                      .toLocalDate()
                                      .equals(fecha)) // Filtra ventas por fecha
                .map(Venta::importeFinal) // Obtiene el importe final de cada venta
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Suma todos los importes
    }
}
