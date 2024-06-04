package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.UserDashboard;
import br.com.supera.smartiot.repository.UserDashboardRepository;
import br.com.supera.smartiot.service.dto.UserDashboardDTO;
import br.com.supera.smartiot.service.mapper.UserDashboardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.UserDashboard}.
 */
@Service
@Transactional
public class UserDashboardService {

    private final Logger log = LoggerFactory.getLogger(UserDashboardService.class);

    private final UserDashboardRepository userDashboardRepository;

    private final UserDashboardMapper userDashboardMapper;

    public UserDashboardService(UserDashboardRepository userDashboardRepository, UserDashboardMapper userDashboardMapper) {
        this.userDashboardRepository = userDashboardRepository;
        this.userDashboardMapper = userDashboardMapper;
    }

    /**
     * Save a userDashboard.
     *
     * @param userDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDashboardDTO save(UserDashboardDTO userDashboardDTO) {
        log.debug("Request to save UserDashboard : {}", userDashboardDTO);
        UserDashboard userDashboard = userDashboardMapper.toEntity(userDashboardDTO);
        userDashboard = userDashboardRepository.save(userDashboard);
        return userDashboardMapper.toDto(userDashboard);
    }

    /**
     * Update a userDashboard.
     *
     * @param userDashboardDTO the entity to save.
     * @return the persisted entity.
     */
    public UserDashboardDTO update(UserDashboardDTO userDashboardDTO) {
        log.debug("Request to update UserDashboard : {}", userDashboardDTO);
        UserDashboard userDashboard = userDashboardMapper.toEntity(userDashboardDTO);
        userDashboard = userDashboardRepository.save(userDashboard);
        return userDashboardMapper.toDto(userDashboard);
    }

    /**
     * Partially update a userDashboard.
     *
     * @param userDashboardDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserDashboardDTO> partialUpdate(UserDashboardDTO userDashboardDTO) {
        log.debug("Request to partially update UserDashboard : {}", userDashboardDTO);

        return userDashboardRepository
            .findById(userDashboardDTO.getId())
            .map(existingUserDashboard -> {
                userDashboardMapper.partialUpdate(existingUserDashboard, userDashboardDTO);

                return existingUserDashboard;
            })
            .map(userDashboardRepository::save)
            .map(userDashboardMapper::toDto);
    }

    /**
     * Get all the userDashboards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDashboardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserDashboards");
        return userDashboardRepository.findAll(pageable).map(userDashboardMapper::toDto);
    }

    /**
     * Get one userDashboard by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserDashboardDTO> findOne(Long id) {
        log.debug("Request to get UserDashboard : {}", id);
        return userDashboardRepository.findById(id).map(userDashboardMapper::toDto);
    }

    /**
     * Delete the userDashboard by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserDashboard : {}", id);
        userDashboardRepository.deleteById(id);
    }
}
