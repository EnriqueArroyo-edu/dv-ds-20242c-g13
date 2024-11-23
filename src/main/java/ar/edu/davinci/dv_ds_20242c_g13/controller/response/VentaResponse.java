package ar.edu.davinci.dv_ds_20242c_g13.controller.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class VentaResponse {
	private Long id;
	private ClienteResponse cliente;
	private String fecha;
	private List<ItemResponse> items;
	private BigDecimal importeFinal;
}

