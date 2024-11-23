package ar.edu.davinci.dv_ds_20242c_g13.service;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
import ar.edu.davinci.dv_ds_20242c_g13.domain.TipoPrenda;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
public interface PrendaService {
	
	Prenda save(Prenda prenda) throws BusinessException;
	Prenda update(final Prenda prenda) throws BusinessException;
	void delete(final Prenda prenda);
	void delete(final Long id);
	Prenda findById(Long id) throws BusinessException;
	List<Prenda> list();
	Page<Prenda> list(Pageable pageable);
	long count();
	List<TipoPrenda> getTipoPrendas();
	BigDecimal calcularPrecioFinal(Long prendaId) throws BusinessException;
	
    

}
