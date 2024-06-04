package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.WaterMeasurement;
import br.com.supera.smartiot.repository.WaterMeasurementRepository;
import br.com.supera.smartiot.service.dto.WaterMeasurementDTO;
import br.com.supera.smartiot.service.mapper.WaterMeasurementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.WaterMeasurement}.
 */
@Service
@Transactional
public class WaterMeasurementService {

    private final Logger log = LoggerFactory.getLogger(WaterMeasurementService.class);

    private final WaterMeasurementRepository waterMeasurementRepository;

    private final WaterMeasurementMapper waterMeasurementMapper;

    public WaterMeasurementService(WaterMeasurementRepository waterMeasurementRepository, WaterMeasurementMapper waterMeasurementMapper) {
        this.waterMeasurementRepository = waterMeasurementRepository;
        this.waterMeasurementMapper = waterMeasurementMapper;
    }

    /**
     * Save a waterMeasurement.
     *
     * @param waterMeasurementDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterMeasurementDTO save(WaterMeasurementDTO waterMeasurementDTO) {
        log.debug("Request to save WaterMeasurement : {}", waterMeasurementDTO);
        WaterMeasurement waterMeasurement = waterMeasurementMapper.toEntity(waterMeasurementDTO);
        waterMeasurement = waterMeasurementRepository.save(waterMeasurement);
        return waterMeasurementMapper.toDto(waterMeasurement);
    }

    /**
     * Update a waterMeasurement.
     *
     * @param waterMeasurementDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterMeasurementDTO update(WaterMeasurementDTO waterMeasurementDTO) {
        log.debug("Request to update WaterMeasurement : {}", waterMeasurementDTO);
        WaterMeasurement waterMeasurement = waterMeasurementMapper.toEntity(waterMeasurementDTO);
        waterMeasurement = waterMeasurementRepository.save(waterMeasurement);
        return waterMeasurementMapper.toDto(waterMeasurement);
    }

    /**
     * Partially update a waterMeasurement.
     *
     * @param waterMeasurementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WaterMeasurementDTO> partialUpdate(WaterMeasurementDTO waterMeasurementDTO) {
        log.debug("Request to partially update WaterMeasurement : {}", waterMeasurementDTO);

        return waterMeasurementRepository
            .findById(waterMeasurementDTO.getId())
            .map(existingWaterMeasurement -> {
                waterMeasurementMapper.partialUpdate(existingWaterMeasurement, waterMeasurementDTO);

                return existingWaterMeasurement;
            })
            .map(waterMeasurementRepository::save)
            .map(waterMeasurementMapper::toDto);
    }

    /**
     * Get all the waterMeasurements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WaterMeasurementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaterMeasurements");
        return waterMeasurementRepository.findAll(pageable).map(waterMeasurementMapper::toDto);
    }

    /**
     * Get one waterMeasurement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WaterMeasurementDTO> findOne(Long id) {
        log.debug("Request to get WaterMeasurement : {}", id);
        return waterMeasurementRepository.findById(id).map(waterMeasurementMapper::toDto);
    }

    /**
     * Delete the waterMeasurement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WaterMeasurement : {}", id);
        waterMeasurementRepository.deleteById(id);
    }
}
