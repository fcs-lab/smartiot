package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.UserContract;
import br.com.supera.smartiot.repository.UserContractRepository;
import br.com.supera.smartiot.service.dto.UserContractDTO;
import br.com.supera.smartiot.service.mapper.UserContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.UserContract}.
 */
@Service
@Transactional
public class UserContractService {

    private final Logger log = LoggerFactory.getLogger(UserContractService.class);

    private final UserContractRepository userContractRepository;

    private final UserContractMapper userContractMapper;

    public UserContractService(UserContractRepository userContractRepository, UserContractMapper userContractMapper) {
        this.userContractRepository = userContractRepository;
        this.userContractMapper = userContractMapper;
    }

    /**
     * Save a userContract.
     *
     * @param userContractDTO the entity to save.
     * @return the persisted entity.
     */
    public UserContractDTO save(UserContractDTO userContractDTO) {
        log.debug("Request to save UserContract : {}", userContractDTO);
        UserContract userContract = userContractMapper.toEntity(userContractDTO);
        userContract = userContractRepository.save(userContract);
        return userContractMapper.toDto(userContract);
    }

    /**
     * Update a userContract.
     *
     * @param userContractDTO the entity to save.
     * @return the persisted entity.
     */
    public UserContractDTO update(UserContractDTO userContractDTO) {
        log.debug("Request to update UserContract : {}", userContractDTO);
        UserContract userContract = userContractMapper.toEntity(userContractDTO);
        userContract = userContractRepository.save(userContract);
        return userContractMapper.toDto(userContract);
    }

    /**
     * Partially update a userContract.
     *
     * @param userContractDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserContractDTO> partialUpdate(UserContractDTO userContractDTO) {
        log.debug("Request to partially update UserContract : {}", userContractDTO);

        return userContractRepository
            .findById(userContractDTO.getId())
            .map(existingUserContract -> {
                userContractMapper.partialUpdate(existingUserContract, userContractDTO);

                return existingUserContract;
            })
            .map(userContractRepository::save)
            .map(userContractMapper::toDto);
    }

    /**
     * Get all the userContracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserContracts");
        return userContractRepository.findAll(pageable).map(userContractMapper::toDto);
    }

    /**
     * Get all the userContracts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserContractDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userContractRepository.findAllWithEagerRelationships(pageable).map(userContractMapper::toDto);
    }

    /**
     * Get one userContract by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserContractDTO> findOne(Long id) {
        log.debug("Request to get UserContract : {}", id);
        return userContractRepository.findOneWithEagerRelationships(id).map(userContractMapper::toDto);
    }

    /**
     * Delete the userContract by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserContract : {}", id);
        userContractRepository.deleteById(id);
    }
}
