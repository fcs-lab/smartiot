package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ApplicationUser;
import br.com.supera.smartiot.domain.UserAccount;
import br.com.supera.smartiot.service.dto.ApplicationUserDTO;
import br.com.supera.smartiot.service.dto.UserAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAccount} and its DTO {@link UserAccountDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserAccountMapper extends EntityMapper<UserAccountDTO, UserAccount> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "applicationUserId")
    UserAccountDTO toDto(UserAccount s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
