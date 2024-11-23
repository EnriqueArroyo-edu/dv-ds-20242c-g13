package ar.edu.davinci.dv_ds_20242c_g13.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ar.edu.davinci.dv_ds_20242c_g13.controller.request.PrendaInsertRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.request.PrendaUpdateRequest;
import ar.edu.davinci.dv_ds_20242c_g13.controller.response.PrendaResponse;
import ar.edu.davinci.dv_ds_20242c_g13.domain.Prenda;
@Mapper
public interface PrendaMapper {
	PrendaMapper instance = Mappers.getMapper(PrendaMapper.class);
	PrendaResponse mapToPrendaResponse(Prenda prenda);
	Prenda mapToPrenda(PrendaInsertRequest prendaDto);
	Prenda mapToPrenda(PrendaUpdateRequest prendaDto);
}
