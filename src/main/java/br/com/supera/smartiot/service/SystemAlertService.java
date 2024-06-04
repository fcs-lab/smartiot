package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.SystemAlert;
import br.com.supera.smartiot.repository.SystemAlertRepository;
import br.com.supera.smartiot.service.dto.SystemAlertDTO;
import br.com.supera.smartiot.service.mapper.SystemAlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.SystemAlert}.
 */
@Service
@Transactional
public class SystemAlertService {

    private final Logger log = LoggerFactory.getLogger(SystemAlertService.class);

    private final SystemAlertRepository systemAlertRepository;

    private final SystemAlertMapper systemAlertMapper;

    public SystemAlertService(SystemAlertRepository systemAlertRepository, SystemAlertMapper systemAlertMapper) {
        this.systemAlertRepository = systemAlertRepository;
        this.systemAlertMapper = systemAlertMapper;
    }

    /**
     * Save a systemAlert.
     *
     * @param systemAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemAlertDTO save(SystemAlertDTO systemAlertDTO) {
        log.debug("Request to save SystemAlert : {}", systemAlertDTO);
        SystemAlert systemAlert = systemAlertMapper.toEntity(systemAlertDTO);
        systemAlert = systemAlertRepository.save(systemAlert);
        return systemAlertMapper.toDto(systemAlert);
    }

    /**
     * Update a systemAlert.
     *
     * @param systemAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemAlertDTO update(SystemAlertDTO systemAlertDTO) {
        log.debug("Request to update SystemAlert : {}", systemAlertDTO);
        SystemAlert systemAlert = systemAlertMapper.toEntity(systemAlertDTO);
        systemAlert = systemAlertRepository.save(systemAlert);
        return systemAlertMapper.toDto(systemAlert);
    }

    /**
     * Partially update a systemAlert.
     *
     * @param systemAlertDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemAlertDTO> partialUpdate(SystemAlertDTO systemAlertDTO) {
        log.debug("Request to partially update SystemAlert : {}", systemAlertDTO);

        return systemAlertRepository
            .findById(systemAlertDTO.getId())
            .map(existingSystemAlert -> {
                systemAlertMapper.partialUpdate(existingSystemAlert, systemAlertDTO);

                return existingSystemAlert;
            })
            .map(systemAlertRepository::save)
            .map(systemAlertMapper::toDto);
    }

    /**
     * Get all the systemAlerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemAlertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemAlerts");
        return systemAlertRepository.findAll(pageable).map(systemAlertMapper::toDto);
    }

    /**
     * Get one systemAlert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemAlertDTO> findOne(Long id) {
        log.debug("Request to get SystemAlert : {}", id);
        return systemAlertRepository.findById(id).map(systemAlertMapper::toDto);
    }

    /**
     * Delete the systemAlert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemAlert : {}", id);
        systemAlertRepository.deleteById(id);
    }
}
