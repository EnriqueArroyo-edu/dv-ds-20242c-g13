package ar.edu.davinci.dv_ds_20242c_g13.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import ar.edu.davinci.dv_ds_20242c_g13.strategy.EstadoPrendaStrategy;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Configuración inical de JPA de una entidad
@SuppressWarnings("deprecation")
@Entity
@Table(name = "prendas")

// Configuración de Lombok
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Prenda implements Serializable {

    private static final long serialVersionUID = -8359168975855133954L;

    // Configurar por JPA cual el PK de la tabla prendas
    @Id
    // Configurar la estrategia de generación de los ids por JPA
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    // Configuramos por JPA el nombre de la columna
    @Column(name = "prd_id")
    private Long id;

    @Column(name = "prd_descripcion", nullable = false)
    private String descripcion;

    @Column(name = "prd_tipo_prenda")
    @Enumerated(EnumType.STRING)
    private TipoPrenda tipo;

    @Column(name = "prd_precio_base", nullable = false)
    private BigDecimal precioBase;

    @Column(name = "prd_estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPrenda estado;
    
    @Column(name = "prd_descuento_promocion")
    private BigDecimal descuentoPromocion;

    @Transient // Este campo no se almacena en la base de datos
    private EstadoPrendaStrategy estadoStrategy;

    /**
     * Calcula el precio final basado en la estrategia asignada.
     * 
     * @return BigDecimal - Precio final de la prenda
     */
    public BigDecimal obtenerPrecioPrenda() {
        if (estadoStrategy == null) {
        	asignarEstrategia();
            throw new IllegalStateException("La estrategia de estado no está definida.");
        }
        return estadoStrategy.obtenerPrecioVenta(precioBase);
    }

    /**
     * Asigna la estrategia correspondiente al estado actual de la prenda.
     * Debe llamarse al crear o actualizar una prenda.
     */
    public void asignarEstrategia() {
        switch (estado) {
            case NUEVA:
                this.estadoStrategy = new ar.edu.davinci.dv_ds_20242c_g13.strategy.PrendaNueva();
                break;
            case PROMOCION:
                if (descuentoPromocion != null && descuentoPromocion.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("El descuento para Promoción no puede ser menor a 0.");
                }
                // Si es nulo, lo tratamos como 0
                if (descuentoPromocion == null) {
                    this.descuentoPromocion = BigDecimal.ZERO;
                }
                this.estadoStrategy = new ar.edu.davinci.dv_ds_20242c_g13.strategy.PrendaPromocion(descuentoPromocion);
                break;
            case LIQUIDACION:
                this.estadoStrategy = new ar.edu.davinci.dv_ds_20242c_g13.strategy.PrendaLiquidacion();
                break;
            default:
                throw new IllegalArgumentException("Estado de prenda no soportado: " + estado);
        }
    }
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prd_stock_id", referencedColumnName = "stock_id")
    private Stock stock;

    public void agregarStock(Integer cantidad) {
        if (stock == null) {
            stock = new Stock();
            stock.setCantidad(0);
        }
        stock.agregarStock(cantidad);
    }

    public void descontarStock(Integer cantidad) {
        if (stock == null) {
            throw new IllegalStateException("La prenda no tiene un stock asociado.");
        }
        stock.descontarStock(cantidad);
    }

    public Integer getCantidad() {
        return stock != null ? stock.getCantidad() : 0;
    }

}
