package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ConfiguracaoAlerta;
import br.com.supera.smartiot.domain.Sensor;
import br.com.supera.smartiot.service.dto.ConfiguracaoAlertaDTO;
import br.com.supera.smartiot.service.dto.SensorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfiguracaoAlerta} and its DTO {@link ConfiguracaoAlertaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfiguracaoAlertaMapper extends EntityMapper<ConfiguracaoAlertaDTO, ConfiguracaoAlerta> {
    @Mapping(target = "sensor", source = "sensor", qualifiedByName = "sensorNome")
    ConfiguracaoAlertaDTO toDto(ConfiguracaoAlerta s);

    @Named("sensorNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    SensorDTO toDtoSensorNome(Sensor sensor);
}
