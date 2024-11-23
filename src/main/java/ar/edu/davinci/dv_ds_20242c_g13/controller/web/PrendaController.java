package ar.edu.davinci.dv_ds_20242c_g13.controller.web;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.ModelAndView;
import ar.edu.davinci.dv_ds_20242c_g13.controller.TiendaApp;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.EstadoPrenda;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20242c_g13.service.PrendaService;
@Controller
public class PrendaController extends TiendaApp {
	private final Logger LOGGER = LoggerFactory.getLogger(PrendaController.class);
	@Autowired
	private PrendaService prendaService;
	@GetMapping(path = "/prendas/list")
	public String showPrendaPage(Model model) {
	    LOGGER.info("GET - showPrendaPage  - /prendas/list");
	    Pageable pageable = PageRequest.of(0, 20);
	    Page<Prenda> prendas = prendaService.list(pageable);
	    prendas.forEach(prenda -> LOGGER.info("Prenda: {}, Descuento: {}", prenda.getDescripcion(), prenda.getDescuentoPromocion()));
	    model.addAttribute("listPrendas", prendas);
	    model.addAttribute("pageNumber", prendas.getPageable().getPageNumber());
	    model.addAttribute("totalPages", prendas.getTotalPages());
	    return "prendas/list_prendas";
	}

	@GetMapping(path = "/prendas/new")
	public String showNewPrendaPage(Model model) {
		LOGGER.info("GET - showNewPrendaPage - /prendas/new");
		Prenda prenda = new Prenda();
		model.addAttribute("prenda", prenda);
		model.addAttribute("tipoPrendas", prendaService.getTipoPrendas());
		LOGGER.info("prendas: " + prenda.toString());
		return "prendas/new_prendas";
	}
	@PostMapping(value = "/prendas/save")
	public String savePrenda(@ModelAttribute("prenda") Prenda prenda) {
	    LOGGER.info("POST - savePrenda - /prendas/save");
	    LOGGER.info("Prenda recibida: " + prenda);

	    try {
	        if (prenda.getTipo() == null) {
	            throw new IllegalArgumentException("El tipo de prenda es obligatorio.");
	        }

	        if (prenda.getEstado() == EstadoPrenda.PROMOCION && 
	            prenda.getDescuentoPromocion() != null && 
	            prenda.getDescuentoPromocion().compareTo(BigDecimal.ZERO) < 0) {
	            throw new IllegalArgumentException("El descuento para Promoción no puede ser menor a 0.");
	        }
	    
	        prenda.asignarEstrategia();

	        if (prenda.getId() == null) {
	            prendaService.save(prenda);
	        } else {
	            prendaService.update(prenda);
	        }
	    } catch (Exception e) {
	        LOGGER.error("Error al guardar la prenda: " + e.getMessage());
	        return "redirect:/tienda/prendas/new?error=true";
	    }

	    return "redirect:/tienda/prendas/list";
	}

	@RequestMapping(value = "/prendas/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditPrendaPage(@PathVariable(name = "id") Long prendaId) {
		LOGGER.info("GET - showEditPrendaPage - /prendas/edit/{id}");
		LOGGER.info("prenda: " + prendaId);
		ModelAndView mav = new ModelAndView("prendas/edit_prendas");
		Prenda prenda = null;
		try {
			prenda = prendaService.findById(prendaId);
			mav.addObject("prenda", prenda);
			mav.addObject("tipoPrendaActual", prenda.getTipo());
		} catch (BusinessException e) {
			LOGGER.error("ERROR AL TRAER LA PRENDA");
			e.printStackTrace();
		}
		mav.addObject("tipoPrendas", prendaService.getTipoPrendas());
		return mav;
	}
	@RequestMapping(value = "/prendas/delete/{id}", method = RequestMethod.GET)
	public String deletePrenda(@PathVariable(name = "id") Long prendaId) {
		LOGGER.info("GET - deletePrenda - /prendas/delete/{id}");
		LOGGER.info("prenda: " + prendaId);
		prendaService.delete(prendaId);
		return "redirect:/tienda/prendas/list";
	}
}