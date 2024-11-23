package ar.edu.davinci.dv_ds_20242c_g13.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venta_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Item implements Serializable {

    private static final long serialVersionUID = 5324396181568770929L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "itm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itm_vta_id", referencedColumnName = "vta_id", nullable = false)
    @JsonBackReference
    private Venta venta;

    @Column(name = "itm_cantidad")
    private Integer cantidad;

    @ManyToOne(targetEntity = Prenda.class, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "itm_prd_id", referencedColumnName = "prd_id", nullable = false)
    private Prenda prenda;

    public BigDecimal importe() {
        if (prenda != null) {
            prenda.asignarEstrategia();
            return prenda.obtenerPrecioPrenda().multiply(new BigDecimal(cantidad));
        }
        return BigDecimal.ZERO;
    }
}
