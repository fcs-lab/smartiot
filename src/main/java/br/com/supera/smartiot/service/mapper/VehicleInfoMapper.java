package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleInfo} and its DTO {@link VehicleInfoDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleInfoMapper extends EntityMapper<VehicleInfoDTO, VehicleInfo> {}
