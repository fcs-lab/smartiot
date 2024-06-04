package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleStatusLog;
import br.com.supera.smartiot.service.dto.VehicleStatusLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleStatusLog} and its DTO {@link VehicleStatusLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleStatusLogMapper extends EntityMapper<VehicleStatusLogDTO, VehicleStatusLog> {}
