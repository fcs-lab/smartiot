package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleGroup;
import br.com.supera.smartiot.service.dto.VehicleGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleGroup} and its DTO {@link VehicleGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleGroupMapper extends EntityMapper<VehicleGroupDTO, VehicleGroup> {}
