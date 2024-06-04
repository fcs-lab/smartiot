package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleDamage;
import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.service.dto.VehicleDamageDTO;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleDamage} and its DTO {@link VehicleDamageDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleDamageMapper extends EntityMapper<VehicleDamageDTO, VehicleDamage> {
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = "vehicleInfoId")
    VehicleDamageDTO toDto(VehicleDamage s);

    @Named("vehicleInfoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VehicleInfoDTO toDtoVehicleInfoId(VehicleInfo vehicleInfo);
}
