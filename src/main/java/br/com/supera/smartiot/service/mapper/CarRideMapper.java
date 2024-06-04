package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ApplicationUser;
import br.com.supera.smartiot.domain.CarRide;
import br.com.supera.smartiot.service.dto.ApplicationUserDTO;
import br.com.supera.smartiot.service.dto.CarRideDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CarRide} and its DTO {@link CarRideDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarRideMapper extends EntityMapper<CarRideDTO, CarRide> {
    @Mapping(target = "driver", source = "driver", qualifiedByName = "applicationUserId")
    CarRideDTO toDto(CarRide s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
