package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.WaterAlert;
import br.com.supera.smartiot.repository.WaterAlertRepository;
import br.com.supera.smartiot.service.dto.WaterAlertDTO;
import br.com.supera.smartiot.service.mapper.WaterAlertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.WaterAlert}.
 */
@Service
@Transactional
public class WaterAlertService {

    private final Logger log = LoggerFactory.getLogger(WaterAlertService.class);

    private final WaterAlertRepository waterAlertRepository;

    private final WaterAlertMapper waterAlertMapper;

    public WaterAlertService(WaterAlertRepository waterAlertRepository, WaterAlertMapper waterAlertMapper) {
        this.waterAlertRepository = waterAlertRepository;
        this.waterAlertMapper = waterAlertMapper;
    }

    /**
     * Save a waterAlert.
     *
     * @param waterAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterAlertDTO save(WaterAlertDTO waterAlertDTO) {
        log.debug("Request to save WaterAlert : {}", waterAlertDTO);
        WaterAlert waterAlert = waterAlertMapper.toEntity(waterAlertDTO);
        waterAlert = waterAlertRepository.save(waterAlert);
        return waterAlertMapper.toDto(waterAlert);
    }

    /**
     * Update a waterAlert.
     *
     * @param waterAlertDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterAlertDTO update(WaterAlertDTO waterAlertDTO) {
        log.debug("Request to update WaterAlert : {}", waterAlertDTO);
        WaterAlert waterAlert = waterAlertMapper.toEntity(waterAlertDTO);
        waterAlert = waterAlertRepository.save(waterAlert);
        return waterAlertMapper.toDto(waterAlert);
    }

    /**
     * Partially update a waterAlert.
     *
     * @param waterAlertDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WaterAlertDTO> partialUpdate(WaterAlertDTO waterAlertDTO) {
        log.debug("Request to partially update WaterAlert : {}", waterAlertDTO);

        return waterAlertRepository
            .findById(waterAlertDTO.getId())
            .map(existingWaterAlert -> {
                waterAlertMapper.partialUpdate(existingWaterAlert, waterAlertDTO);

                return existingWaterAlert;
            })
            .map(waterAlertRepository::save)
            .map(waterAlertMapper::toDto);
    }

    /**
     * Get all the waterAlerts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WaterAlertDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaterAlerts");
        return waterAlertRepository.findAll(pageable).map(waterAlertMapper::toDto);
    }

    /**
     * Get one waterAlert by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WaterAlertDTO> findOne(Long id) {
        log.debug("Request to get WaterAlert : {}", id);
        return waterAlertRepository.findById(id).map(waterAlertMapper::toDto);
    }

    /**
     * Delete the waterAlert by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WaterAlert : {}", id);
        waterAlertRepository.deleteById(id);
    }
}
