package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.domain.VehicleService;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import br.com.supera.smartiot.service.dto.VehicleServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleService} and its DTO {@link VehicleServiceDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleServiceMapper extends EntityMapper<VehicleServiceDTO, VehicleService> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleInfoId")
    VehicleServiceDTO toDto(VehicleService s);

    @Named("vehicleInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleInfoDTO toDtoVehicleInfoId(VehicleInfo vehicleInfo);
}
