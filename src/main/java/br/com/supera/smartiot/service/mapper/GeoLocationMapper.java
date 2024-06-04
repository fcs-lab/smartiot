package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.GeoLocation;
import br.com.supera.smartiot.service.dto.GeoLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link GeoLocation} and its DTO {@link GeoLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface GeoLocationMapper extends EntityMapper<GeoLocationDTO, GeoLocation> {}
