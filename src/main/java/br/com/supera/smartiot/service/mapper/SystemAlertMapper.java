package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.SystemAlert;
import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.service.dto.SystemAlertDTO;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemAlert} and its DTO {@link SystemAlertDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemAlertMapper extends EntityMapper<SystemAlertDTO, SystemAlert> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleInfoId")
    SystemAlertDTO toDto(SystemAlert s);

    @Named("vehicleInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleInfoDTO toDtoVehicleInfoId(VehicleInfo vehicleInfo);
}
