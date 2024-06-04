package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.WaterSensor;
import br.com.supera.smartiot.service.dto.WaterSensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaterSensor} and its DTO {@link WaterSensorDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaterSensorMapper extends EntityMapper<WaterSensorDTO, WaterSensor> {}
