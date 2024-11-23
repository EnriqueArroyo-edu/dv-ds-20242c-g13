package ar.edu.davinci.dv_ds_20242c_g13.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(path="/tienda/api")
public abstract class TiendaAppRest {
	// Esto es equivalente a  hacer
	// http://localhost:8090/tienda/api
	// El puerto 8090, está configurado en la propiedad server.port
}
