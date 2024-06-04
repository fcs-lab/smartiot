package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.UserRole;
import br.com.supera.smartiot.service.dto.UserRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRole} and its DTO {@link UserRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRoleMapper extends EntityMapper<UserRoleDTO, UserRole> {}
