package br.com.supera.smartiot.service.mapper;

import br.com.supera.smartiot.domain.ApplicationUser;
import br.com.supera.smartiot.domain.User;
import br.com.supera.smartiot.domain.UserContract;
import br.com.supera.smartiot.service.dto.ApplicationUserDTO;
import br.com.supera.smartiot.service.dto.UserContractDTO;
import br.com.supera.smartiot.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userEmail")
    @Mapping(target = "contracts", source = "contracts", qualifiedByName = "userContractIdSet")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Mapping(target = "contracts", ignore = true)
    @Mapping(target = "removeContracts", ignore = true)
    ApplicationUser toEntity(ApplicationUserDTO applicationUserDTO);

    @Named("userEmail")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    UserDTO toDtoUserEmail(User user);

    @Named("userContractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserContractDTO toDtoUserContractId(UserContract userContract);

    @Named("userContractIdSet")
    default Set<UserContractDTO> toDtoUserContractIdSet(Set<UserContract> userContract) {
        return userContract.stream().map(this::toDtoUserContractId).collect(Collectors.toSet());
    }
}
