package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleModel;
import br.com.supera.smartiot.service.dto.VehicleModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleModel} and its DTO {@link VehicleModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleModelMapper extends EntityMapper<VehicleModelDTO, VehicleModel> {}
