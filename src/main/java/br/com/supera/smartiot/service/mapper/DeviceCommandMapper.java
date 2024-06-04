package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.DeviceCommand;
import br.com.supera.smartiot.service.dto.DeviceCommandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceCommand} and its DTO {@link DeviceCommandDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceCommandMapper extends EntityMapper<DeviceCommandDTO, DeviceCommand> {}
