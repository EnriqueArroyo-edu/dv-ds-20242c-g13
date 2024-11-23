package ar.edu.davinci.dv_ds_20242c_g13.controller.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrendaInsertRequest {

	private String descripcion;

	private String tipo;

	private BigDecimal precioBase;

}
