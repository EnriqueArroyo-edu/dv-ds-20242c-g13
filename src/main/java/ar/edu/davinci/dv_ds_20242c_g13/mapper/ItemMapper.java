package ar.edu.davinci.dv_ds_20242c_g13.mapper;

import java.util.Collection;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ar.edu.davinci.dv_ds_20242c_g13.controller.request.ItemInsertRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.request.ItemUpdateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.response.ItemResponse;
import ar.edu.davinci.dv_ds_20242c_g13.controller.web.request.VentaItemCreateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Item;

@Mapper
public interface ItemMapper {
	ItemMapper instance = Mappers.getMapper(ItemMapper.class);

	@Mapping(target = "importe", expression = "java(new java.math.BigDecimal(item.importe().doubleValue()))")
	@Mapping(target = "prenda", source = "prenda")
	ItemResponse mapToItemResponse(Item item);

	@Mapping(target = "prenda.id", source = "prendaId")
	Item matToItem(ItemInsertRequest itemDto);

	Item matToItem(ItemUpdateRequest itemDto);

	@Mapping(target = "importe", expression = "java(new java.math.BigDecimal(item.importe().doubleValue()))")
	@Mapping(target = "prenda", source = "prenda")
	List<ItemResponse> mapToItemRespons(Collection<Item> itmens);

	@Mapping(target = "prenda.id", source = "prendaId")
	Item matToItem(VentaItemCreateRequest ventaItemCreateRequest);
}
