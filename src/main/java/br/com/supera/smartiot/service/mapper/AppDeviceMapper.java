package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.AppDevice;
import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.service.dto.AppDeviceDTO;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppDevice} and its DTO {@link AppDeviceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppDeviceMapper extends EntityMapper<AppDeviceDTO, AppDevice> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleInfoId")
    AppDeviceDTO toDto(AppDevice s);

    @Named("vehicleInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleInfoDTO toDtoVehicleInfoId(VehicleInfo vehicleInfo);
}
