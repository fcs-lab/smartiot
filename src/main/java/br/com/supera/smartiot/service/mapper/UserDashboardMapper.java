package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.UserDashboard;
import br.com.supera.smartiot.service.dto.UserDashboardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDashboard} and its DTO {@link UserDashboardDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserDashboardMapper extends EntityMapper<UserDashboardDTO, UserDashboard> {}
