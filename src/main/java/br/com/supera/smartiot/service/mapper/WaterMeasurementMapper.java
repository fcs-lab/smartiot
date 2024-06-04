package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.WaterMeasurement;
import br.com.supera.smartiot.service.dto.WaterMeasurementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaterMeasurement} and its DTO {@link WaterMeasurementDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaterMeasurementMapper extends EntityMapper<WaterMeasurementDTO, WaterMeasurement> {}
