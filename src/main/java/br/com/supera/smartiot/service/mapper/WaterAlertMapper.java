package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.WaterAlert;
import br.com.supera.smartiot.service.dto.WaterAlertDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaterAlert} and its DTO {@link WaterAlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaterAlertMapper extends EntityMapper<WaterAlertDTO, WaterAlert> {}
