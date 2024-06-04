package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.UserContract;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserContractRepositoryWithBagRelationshipsImpl implements UserContractRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERCONTRACTS_PARAMETER = "userContracts";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserContract> fetchBagRelationships(Optional<UserContract> userContract) {
        return userContract.map(this::fetchUsers);
    }

    @Override
    public Page<UserContract> fetchBagRelationships(Page<UserContract> userContracts) {
        return new PageImpl<>(
            fetchBagRelationships(userContracts.getContent()),
            userContracts.getPageable(),
            userContracts.getTotalElements()
        );
    }

    @Override
    public List<UserContract> fetchBagRelationships(List<UserContract> userContracts) {
        return Optional.of(userContracts).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    UserContract fetchUsers(UserContract result) {
        return entityManager
            .createQuery(
                "select userContract from UserContract userContract left join fetch userContract.users where userContract.id = :id",
                UserContract.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserContract> fetchUsers(List<UserContract> userContracts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userContracts.size()).forEach(index -> order.put(userContracts.get(index).getId(), index));
        List<UserContract> result = entityManager
            .createQuery(
                "select userContract from UserContract userContract left join fetch userContract.users where userContract in :userContracts",
                UserContract.class
            )
            .setParameter(USERCONTRACTS_PARAMETER, userContracts)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
