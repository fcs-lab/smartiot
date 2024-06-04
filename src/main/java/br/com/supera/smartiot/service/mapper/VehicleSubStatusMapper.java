package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleSubStatus;
import br.com.supera.smartiot.service.dto.VehicleSubStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleSubStatus} and its DTO {@link VehicleSubStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleSubStatusMapper extends EntityMapper<VehicleSubStatusDTO, VehicleSubStatus> {}
