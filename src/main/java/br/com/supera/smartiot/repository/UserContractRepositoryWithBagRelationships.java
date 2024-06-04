package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.UserContract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserContractRepositoryWithBagRelationships {
    Optional<UserContract> fetchBagRelationships(Optional<UserContract> userContract);

    List<UserContract> fetchBagRelationships(List<UserContract> userContracts);

    Page<UserContract> fetchBagRelationships(Page<UserContract> userContracts);
}
