package ar.edu.davinci.dv_ds_20242c_g13.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "stock")
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(name = "stk_cantidad", nullable = false)
    private Integer cantidad;

    public void agregarStock(Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a agregar debe ser mayor a 0.");
        }
        this.cantidad += cantidad;
    }

    public void descontarStock(Integer cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0.");
        }
        if (this.cantidad < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para descontar la cantidad solicitada.");
        }
        this.cantidad -= cantidad;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }
}
