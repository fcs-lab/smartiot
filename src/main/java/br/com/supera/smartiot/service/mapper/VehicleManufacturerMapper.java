package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleManufacturer;
import br.com.supera.smartiot.service.dto.VehicleManufacturerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleManufacturer} and its DTO {@link VehicleManufacturerDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleManufacturerMapper extends EntityMapper<VehicleManufacturerDTO, VehicleManufacturer> {}
