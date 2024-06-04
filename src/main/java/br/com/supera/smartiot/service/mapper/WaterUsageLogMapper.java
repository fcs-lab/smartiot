package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.WaterUsageLog;
import br.com.supera.smartiot.service.dto.WaterUsageLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WaterUsageLog} and its DTO {@link WaterUsageLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface WaterUsageLogMapper extends EntityMapper<WaterUsageLogDTO, WaterUsageLog> {}
