package ar.edu.davinci.dv_ds_20242c_g13.controller.web;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.davinci.dv_ds_20242c_g13.Constantes;
import ar.edu.davinci.dv_ds_20242c_g13.controller.TiendaApp;
import ar.edu.davinci.dv_ds_20242c_g13.controller.response.VentaEfectivoResponse;
import ar.edu.davinci.dv_ds_20242c_g13.controller.response.VentaResponse;
import ar.edu.davinci.dv_ds_20242c_g13.controller.response.VentaTarjetaResponse;
import ar.edu.davinci.dv_ds_20242c_g13.controller.web.request.VentaEfectivoCreateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.web.request.VentaItemCreateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.web.request.VentaTarjetaCreateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Venta;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaEfectivo;
import ar.edu.davinci.dv_ds_20242c_g13.domain.VentaTarjeta;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20242c_g13.mapper.ItemMapper;
import ar.edu.davinci.dv_ds_20242c_g13.mapper.PrendaMapper;
import ar.edu.davinci.dv_ds_20242c_g13.mapper.VentaMapper;
import ar.edu.davinci.dv_ds_20242c_g13.service.ItemService;
import ar.edu.davinci.dv_ds_20242c_g13.service.PrendaService;
import ar.edu.davinci.dv_ds_20242c_g13.service.VentaService;
//import ma.glasnost.orika.MapperFacade;
@Controller
public class VentaController extends TiendaApp {
	private final Logger LOGGER = LoggerFactory.getLogger(VentaController.class);
	@Autowired
	private VentaService ventaService;
	@Autowired
	private PrendaService prendaService;
	@Autowired
	private ItemService itemService;
	
	private final VentaMapper ventaMapper = VentaMapper.instance;
	private final ItemMapper itemMapper = ItemMapper.instance;
//    @Autowired
//    private MapperFacade mapper;
	@GetMapping(path = "ventas/list")
	public String showVentaPage(Model model) {
		LOGGER.info("GET - showVentaPage  - /ventas/list");
		Pageable pageable = PageRequest.of(0, 20);
		Page<Venta> ventas = ventaService.list(pageable);
		LOGGER.info("GET - showVentaPage venta importe final: " + ventas.getContent().toString());
		model.addAttribute("listVentas", ventas);
		LOGGER.info("ventas.size: " + ventas.getNumberOfElements());
		return "ventas/list_ventas";
	}
	@GetMapping(path = "/ventas/efectivo/new")
	public String showNewVentaEfectivoPage(Model model) {
		LOGGER.info("GET - showNewVentaPage - /ventas/efectivo/new");
       VentaEfectivoCreateRequest venta = new VentaEfectivoCreateRequest();
       Calendar calendar = Calendar.getInstance();
       Date toDay = calendar.getTime();
		DateFormat formatearFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);
       String fecha = formatearFecha.format(toDay);
		venta.setFecha(fecha);
       model.addAttribute("venta", venta);
       LOGGER.info("ventas: " + venta.toString());
		return "ventas/new_ventas_efectivo";
	}
	@GetMapping(path = "/ventas/tarjeta/new")
	public String showNewVentaTarjetaPage(Model model) {
		LOGGER.info("GET - showNewVentaPage - /ventas/tarjeta/new");
		VentaTarjetaCreateRequest venta = new VentaTarjetaCreateRequest();
       Calendar calendar = Calendar.getInstance();
       Date toDay = calendar.getTime();
		DateFormat formatearFecha = new SimpleDateFormat(Constantes.FORMATO_FECHA);
       String fecha = formatearFecha.format(toDay);
		venta.setFecha(fecha);
       model.addAttribute("venta", venta);
       LOGGER.info("ventas: " + venta.toString());
		return "ventas/new_ventas_tarjeta";
	}
	@GetMapping(path = "/ventas/{ventaId}/item/new")
	public String showNewItemPage(Model model, @PathVariable(name = "ventaId") Long ventaId) {
		LOGGER.info("GET - showNewItemPage - /venta/"+ventaId+"/item/new");
		VentaItemCreateRequest item = new VentaItemCreateRequest();
		item.setVentaId(ventaId);
       model.addAttribute("item", item);
       LOGGER.info("item: " + item.toString());
		return "ventas/new_ventas_item";
	}
	@PostMapping(value = "/ventas/efectivo/save")
	public String saveVentaEfectivo(@ModelAttribute("venta") VentaEfectivoCreateRequest datosVenta) {
	    LOGGER.info("POST - saveVenta - /ventas/efectivo/save");
	    LOGGER.info("datosVenta: " + datosVenta.toString());

	    VentaEfectivo venta = ventaMapper.mapToVentaEfectivo(datosVenta);

	    try {
	        venta = (VentaEfectivo) ventaService.save(venta);
	    } catch (Exception e) {
	        LOGGER.error("Error al guardar la venta: {}", e.getMessage());
	        e.printStackTrace();
	    }

	    return "redirect:/tienda/ventas/list";
	}

	@PostMapping(value = "/ventas/tarjeta/save")
	public String saveVentaTarjeta(@ModelAttribute("venta") VentaTarjetaCreateRequest datosVenta) {
		LOGGER.info("POST - saveVenta - /ventas/tarjeta/save");
		LOGGER.info("venta: " + datosVenta.toString());
       VentaTarjeta venta = ventaMapper.mapToVentaTarjeta(datosVenta);
		//VentaTarjeta venta = mapper.map(datosVenta, VentaTarjeta.class);
       // Grabar el nuevo Venta
       try {
           venta = ventaService.save(venta);
       } catch (Exception e) {
           e.printStackTrace();
       }
		return "redirect:/tienda/ventas/list";
	}
	
	@GetMapping(path = "/ventas/por-fecha")
	public String showVentasPorFecha(@RequestParam(required = false) String fecha, Model model) {
	    LOGGER.info("GET - showVentasPorFecha - /ventas/por-fecha");

	    if (fecha != null) {
	        LocalDate fechaSeleccionada = LocalDate.parse(fecha);
	        List<Venta> ventasDelDia = ventaService.obtenerVentasPorFecha(fechaSeleccionada);

	        BigDecimal totalEfectivo = ventasDelDia.stream()
	                .filter(venta -> venta instanceof VentaEfectivo)
	                .map(Venta::importeFinal)
	                .reduce(BigDecimal.ZERO, BigDecimal::add);

	        BigDecimal totalTarjeta = ventasDelDia.stream()
	                .filter(venta -> venta instanceof VentaTarjeta)
	                .map(Venta::importeFinal)
	                .reduce(BigDecimal.ZERO, BigDecimal::add);

	        model.addAttribute("ventasDelDia", ventasDelDia);
	        model.addAttribute("totalEfectivo", totalEfectivo);
	        model.addAttribute("totalTarjeta", totalTarjeta);
	    } else {
	        model.addAttribute("ventasDelDia", new ArrayList<>());
	        model.addAttribute("totalEfectivo", BigDecimal.ZERO);
	        model.addAttribute("totalTarjeta", BigDecimal.ZERO);
	    }

	    return "ventas/ventas_por_fecha";
	}
	
	@RequestMapping(value = "/ventas/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditVentaPage(@PathVariable(name = "id") Long ventaId) {
		LOGGER.info("GET - showEditVentaPage - /ventas/edit/{id}");
		LOGGER.info("venta: " + ventaId);
		ModelAndView mav = new ModelAndView("ventas/edit_ventas");
		try {
			Venta venta = ventaService.findById(ventaId);
			mav.addObject("venta", venta);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}
	@PostMapping(value = "/ventas/item/save")
	public String saveVentaItem(@ModelAttribute("item") VentaItemCreateRequest datosVentaItem) {
	    LOGGER.info("POST - saveVentaItem - ventas/item/save");
	    LOGGER.info("datosVentaItem: " + datosVentaItem.toString());

	    try {
	        // Crear un nuevo Item
	        Item item = itemMapper.matToItem(datosVentaItem);

	        // Recuperar la prenda asociada usando el servicio
	        Prenda prenda = prendaService.findById(datosVentaItem.getPrendaId());
	        if (prenda == null) {
	            LOGGER.error("La prenda con id {} no existe.", datosVentaItem.getPrendaId());
	            return "redirect:/tienda/ventas/show/" + datosVentaItem.getVentaId() + "?error=prendaNotFound";
	        }
	        item.setPrenda(prenda); // Asigna la prenda recuperada

	        // Verificar si hay suficiente stock
	        if (prenda.getStock() == null || prenda.getStock().getCantidad() < datosVentaItem.getCantidad()) {
	            LOGGER.error("Stock insuficiente para la prenda: {}", prenda.getDescripcion());
	            return "redirect:/tienda/ventas/show/" + datosVentaItem.getVentaId() + "?error=stock";
	        }

	        // Descontar el stock
	        prenda.getStock().descontarStock(datosVentaItem.getCantidad());

	        // Guardar el ítem en la venta
	        Venta venta = ventaService.addItem(datosVentaItem.getVentaId(), item);

	        LOGGER.info("Venta guardada: {}", venta);
	        return "redirect:/tienda/ventas/show/" + datosVentaItem.getVentaId();
	    } catch (Exception e) {
	        LOGGER.error("Error al guardar el ítem de venta: {}", e.getMessage());
	        return "redirect:/tienda/ventas/list?error=true";
	    }
	}


	@GetMapping(path = "/ventas/show/{id}")
	public ModelAndView showVentaPage(@PathVariable(name = "id") Long ventaId) {
		LOGGER.info("GET - showVentaPage  - /tienda/ventas/show/{id}");
		LOGGER.info("venta: " + ventaId);
		ModelAndView mav = new ModelAndView("ventas/show_ventas");
		Venta venta = null;
		try {
			venta = ventaService.findById(ventaId);
			mav.addObject("venta", venta);
		} catch (BusinessException e1) {
           LOGGER.error(e1.getMessage());
			e1.printStackTrace();
		}
       List<VentaResponse> ventasToResponse = new ArrayList<VentaResponse>();
       	// Convertir Venta en VentaResponse
           try {
               if (venta instanceof VentaEfectivo) {
                   VentaEfectivoResponse ventaResponse = ventaMapper.mapToVentaEfectivoResponse((VentaEfectivo) venta);
               	//VentaEfectivoResponse ventaResponse = mapper.map((VentaEfectivo) venta, VentaEfectivoResponse.class);
                   ventasToResponse.add(ventaResponse);
               } else if (venta instanceof VentaTarjeta) {
               	VentaTarjetaResponse ventaResponse = ventaMapper.mapToVentaTarjetaResponse((VentaTarjeta) venta);
               	//VentaTarjetaResponse ventaResponse = mapper.map((VentaTarjeta) venta, VentaTarjetaResponse.class);
                   ventasToResponse.add(ventaResponse);
               }
           } catch (Exception e) {
               LOGGER.error(e.getMessage());
               e.printStackTrace();
           }
           mav.addObject("listVentas", ventasToResponse);
		return mav;
	}
	@RequestMapping(value = "/ventas/delete/{id}", method = RequestMethod.GET)
	public String deleteVenta(@PathVariable(name = "id") Long ventaId) {
	    LOGGER.info("GET - deleteVenta - /ventas/delete/{id}");
	    LOGGER.info("venta: " + ventaId);

	    try {
	        Venta venta = ventaService.findById(ventaId);

	        // Recuperar el stock de todas las prendas asociadas
	        for (Item item : venta.getItems()) {
	            item.getPrenda().agregarStock(item.getCantidad());
	        }

	        // Eliminar la venta
	        ventaService.delete(ventaId);
	        return "redirect:/tienda/ventas/list";
	    } catch (Exception e) {
	        LOGGER.error("Error al eliminar la venta: {}", e.getMessage());
	        return "redirect:/tienda/ventas/list?error=true";
	    }
	}

	@RequestMapping(value = "/item/delete/{id}", method = RequestMethod.GET)
	public String deleteItem(@PathVariable(name = "id") Long itemId) {
	    LOGGER.info("Get - deleteItem - /item/delete/{id}");
	    LOGGER.info("item: " + itemId);
	    try {
	        // Buscar el ítem a eliminar
	        Item item = itemService.findById(itemId);
	        if (item == null) {
	            LOGGER.error("El ítem con id {} no existe.", itemId);
	            return "redirect:/tienda/ventas/list?error=itemNotFound";
	        }

	        // Recuperar el stock de la prenda asociada
	        Prenda prenda = item.getPrenda();
	        if (prenda != null && prenda.getStock() != null) {
	            prenda.getStock().agregarStock(item.getCantidad());
	            LOGGER.info("Stock recuperado para la prenda {}: {} unidades", prenda.getDescripcion(), item.getCantidad());
	        } else {
	            LOGGER.warn("La prenda asociada al ítem no tiene stock registrado.");
	        }

	        // Eliminar el ítem
	        itemService.delete(itemId);

	    } catch (Exception e) {
	        LOGGER.error("Error al eliminar el item: {}", e.getMessage());
	        return "redirect:/tienda/ventas/list?error=true";
	    }
	    return "redirect:/tienda/ventas/list";
	}

	
	@RequestMapping(value = "/ventas/deleteItem/{id}", method = RequestMethod.POST)
	public String deleteVentaItem(@PathVariable(name = "id") Long itemId) {
	    LOGGER.info("Eliminando el item de venta con id: {}", itemId);

	    try {
	        // Buscar el ítem por su ID
	        Item item = itemService.findById(itemId);

	        // Recuperar el stock de la prenda
	        item.getPrenda().agregarStock(item.getCantidad());

	        // Eliminar el ítem de la venta
	        Venta venta = item.getVenta();
	        venta.eliminarItem(item);

	        // Guardar la venta sin el ítem
	        ventaService.save(venta);

	        return "redirect:/tienda/ventas/show/" + venta.getId();
	    } catch (Exception e) {
	        LOGGER.error("Error al eliminar el ítem de venta: {}", e.getMessage());
	        return "redirect:/tienda/ventas/list?error=true";
	    }
	}

}

