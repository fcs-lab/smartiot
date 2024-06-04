package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.DeviceTelemetry;
import br.com.supera.smartiot.service.dto.DeviceTelemetryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceTelemetry} and its DTO {@link DeviceTelemetryDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeviceTelemetryMapper extends EntityMapper<DeviceTelemetryDTO, DeviceTelemetry> {}
