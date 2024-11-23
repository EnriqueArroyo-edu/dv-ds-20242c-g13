package ar.edu.davinci.dv_ds_20242c_g13.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;
import ar.edu.davinci.dv_ds_20242c_g13.exceptions.BusinessException;
public interface ItemService {
	Item save(Item item) throws BusinessException;
	Item update(Item item) throws BusinessException;
	public void delete(Item item);
	public void delete(Long id);
	public Item findById(Long id) throws BusinessException;
	public List<Item> listAll();
	public Page<Item> list(Pageable pageable);
	public long count();
}

