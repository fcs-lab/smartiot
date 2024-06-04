package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ApplicationUser;
import br.com.supera.smartiot.domain.UserContract;
import br.com.supera.smartiot.service.dto.ApplicationUserDTO;
import br.com.supera.smartiot.service.dto.UserContractDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserContract} and its DTO {@link UserContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserContractMapper extends EntityMapper<UserContractDTO, UserContract> {
    @Mapping(target = "users", source = "users", qualifiedByName = "applicationUserIdSet")
    UserContractDTO toDto(UserContract s);

    @Mapping(target = "removeUser", ignore = true)
    UserContract toEntity(UserContractDTO userContractDTO);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);

    @Named("applicationUserIdSet")
    default Set<ApplicationUserDTO> toDtoApplicationUserIdSet(Set<ApplicationUser> applicationUser) {
        return applicationUser.stream().map(this::toDtoApplicationUserId).collect(Collectors.toSet());
    }
}
