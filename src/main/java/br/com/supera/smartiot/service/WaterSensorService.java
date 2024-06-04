package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.WaterSensor;
import br.com.supera.smartiot.repository.WaterSensorRepository;
import br.com.supera.smartiot.service.dto.WaterSensorDTO;
import br.com.supera.smartiot.service.mapper.WaterSensorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.WaterSensor}.
 */
@Service
@Transactional
public class WaterSensorService {

    private final Logger log = LoggerFactory.getLogger(WaterSensorService.class);

    private final WaterSensorRepository waterSensorRepository;

    private final WaterSensorMapper waterSensorMapper;

    public WaterSensorService(WaterSensorRepository waterSensorRepository, WaterSensorMapper waterSensorMapper) {
        this.waterSensorRepository = waterSensorRepository;
        this.waterSensorMapper = waterSensorMapper;
    }

    /**
     * Save a waterSensor.
     *
     * @param waterSensorDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterSensorDTO save(WaterSensorDTO waterSensorDTO) {
        log.debug("Request to save WaterSensor : {}", waterSensorDTO);
        WaterSensor waterSensor = waterSensorMapper.toEntity(waterSensorDTO);
        waterSensor = waterSensorRepository.save(waterSensor);
        return waterSensorMapper.toDto(waterSensor);
    }

    /**
     * Update a waterSensor.
     *
     * @param waterSensorDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterSensorDTO update(WaterSensorDTO waterSensorDTO) {
        log.debug("Request to update WaterSensor : {}", waterSensorDTO);
        WaterSensor waterSensor = waterSensorMapper.toEntity(waterSensorDTO);
        waterSensor = waterSensorRepository.save(waterSensor);
        return waterSensorMapper.toDto(waterSensor);
    }

    /**
     * Partially update a waterSensor.
     *
     * @param waterSensorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WaterSensorDTO> partialUpdate(WaterSensorDTO waterSensorDTO) {
        log.debug("Request to partially update WaterSensor : {}", waterSensorDTO);

        return waterSensorRepository
            .findById(waterSensorDTO.getId())
            .map(existingWaterSensor -> {
                waterSensorMapper.partialUpdate(existingWaterSensor, waterSensorDTO);

                return existingWaterSensor;
            })
            .map(waterSensorRepository::save)
            .map(waterSensorMapper::toDto);
    }

    /**
     * Get all the waterSensors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WaterSensorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaterSensors");
        return waterSensorRepository.findAll(pageable).map(waterSensorMapper::toDto);
    }

    /**
     * Get one waterSensor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WaterSensorDTO> findOne(Long id) {
        log.debug("Request to get WaterSensor : {}", id);
        return waterSensorRepository.findById(id).map(waterSensorMapper::toDto);
    }

    /**
     * Delete the waterSensor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WaterSensor : {}", id);
        waterSensorRepository.deleteById(id);
    }
}
