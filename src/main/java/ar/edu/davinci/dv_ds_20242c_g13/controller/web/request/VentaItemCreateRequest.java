package ar.edu.davinci.dv_ds_20242c_g13.controller.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaItemCreateRequest {
	private Long ventaId;
	private Integer cantidad;
	private Long prendaId;
}

