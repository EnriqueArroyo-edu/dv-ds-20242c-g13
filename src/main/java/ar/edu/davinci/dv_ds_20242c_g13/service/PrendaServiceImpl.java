package ar.edu.davinci.dv_ds_20242c_g13.service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
import ar.edu.davinci.dv_ds_20242c_g13.repository.PrendaRepository;
@Service
public class PrendaServiceImpl implements PrendaService {
	private final Logger LOGGER = LoggerFactory.getLogger(PrendaServiceImpl.class);
	private final PrendaRepository repository;
	@Autowired
	public PrendaServiceImpl(final PrendaRepository repository) {
		this.repository = repository;
	}
	@Override
	public Prenda save(final Prenda prenda) throws BusinessException {
	    LOGGER.debug("Guardamos la prenda: " + prenda.toString());
	    if (prenda.getTipo() == null) {
	        throw new BusinessException("El tipo de prenda es obligatorio.");
	    }
	    if (prenda.getId() == null) {
	        return repository.save(prenda);
	    }
	    throw new BusinessException("No se puede crear la prenda con un ID específico.");
	}
	@Override
	public Prenda update(final Prenda prenda) throws BusinessException {
		LOGGER.debug("Modificamos la prenda: " + prenda.toString());
		if (prenda.getId() != null) {
			return repository.save(prenda);
		}
		throw new BusinessException("No se puede modificar una prenda que aún no fue creada.");
	}
	@Override
	public void delete(final Prenda prenda) {
		LOGGER.debug("Borramos la prenda: " + prenda.toString());
		repository.delete(prenda);
	}
	public void delete(final Long id) {
		LOGGER.debug("Borramos la prenda por id: " + id);
		repository.deleteById(id);
	}
	  @Override
	    public Prenda findById(Long id) throws BusinessException {
	        Optional<Prenda> prenda = repository.findById(id);
	        if (prenda.isEmpty()) {
	            throw new BusinessException("No se encontró la prenda con id: " + id);
	        }
	        return prenda.get();
	    }
	@Override
	public List<Prenda> list() {
		LOGGER.debug("Listado de todas las prendas");
		return repository.findAll();
	}
	@Override
	public Page<Prenda> list(Pageable pageable) {
		LOGGER.debug("Listado de todas las prendas por páginas");
		LOGGER.debug("Pageable: offset: " + pageable.getOffset() + ", pageSize: " + pageable.getPageSize() + " and pageNumber: " + pageable.getPageNumber());
		return repository.findAll(pageable);
	}
	@Override
	public long count() {
		return repository.count();
	}
	@Override
	public List<TipoPrenda> getTipoPrendas() {
		return TipoPrenda.getTipoPrendas();
	}
	
	
	@Override
	public BigDecimal calcularPrecioFinal(Long prendaId) throws BusinessException {
	    Prenda prenda = findById(prendaId);
	    prenda.asignarEstrategia(); // Asegurar que tenga asignada la estrategia
	    return prenda.obtenerPrecioPrenda();
	}
}
