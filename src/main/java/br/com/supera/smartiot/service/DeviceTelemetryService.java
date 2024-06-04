package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.DeviceTelemetry;
import br.com.supera.smartiot.repository.DeviceTelemetryRepository;
import br.com.supera.smartiot.service.dto.DeviceTelemetryDTO;
import br.com.supera.smartiot.service.mapper.DeviceTelemetryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.DeviceTelemetry}.
 */
@Service
@Transactional
public class DeviceTelemetryService {

    private final Logger log = LoggerFactory.getLogger(DeviceTelemetryService.class);

    private final DeviceTelemetryRepository deviceTelemetryRepository;

    private final DeviceTelemetryMapper deviceTelemetryMapper;

    public DeviceTelemetryService(DeviceTelemetryRepository deviceTelemetryRepository, DeviceTelemetryMapper deviceTelemetryMapper) {
        this.deviceTelemetryRepository = deviceTelemetryRepository;
        this.deviceTelemetryMapper = deviceTelemetryMapper;
    }

    /**
     * Save a deviceTelemetry.
     *
     * @param deviceTelemetryDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceTelemetryDTO save(DeviceTelemetryDTO deviceTelemetryDTO) {
        log.debug("Request to save DeviceTelemetry : {}", deviceTelemetryDTO);
        DeviceTelemetry deviceTelemetry = deviceTelemetryMapper.toEntity(deviceTelemetryDTO);
        deviceTelemetry = deviceTelemetryRepository.save(deviceTelemetry);
        return deviceTelemetryMapper.toDto(deviceTelemetry);
    }

    /**
     * Update a deviceTelemetry.
     *
     * @param deviceTelemetryDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceTelemetryDTO update(DeviceTelemetryDTO deviceTelemetryDTO) {
        log.debug("Request to update DeviceTelemetry : {}", deviceTelemetryDTO);
        DeviceTelemetry deviceTelemetry = deviceTelemetryMapper.toEntity(deviceTelemetryDTO);
        deviceTelemetry = deviceTelemetryRepository.save(deviceTelemetry);
        return deviceTelemetryMapper.toDto(deviceTelemetry);
    }

    /**
     * Partially update a deviceTelemetry.
     *
     * @param deviceTelemetryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeviceTelemetryDTO> partialUpdate(DeviceTelemetryDTO deviceTelemetryDTO) {
        log.debug("Request to partially update DeviceTelemetry : {}", deviceTelemetryDTO);

        return deviceTelemetryRepository
            .findById(deviceTelemetryDTO.getId())
            .map(existingDeviceTelemetry -> {
                deviceTelemetryMapper.partialUpdate(existingDeviceTelemetry, deviceTelemetryDTO);

                return existingDeviceTelemetry;
            })
            .map(deviceTelemetryRepository::save)
            .map(deviceTelemetryMapper::toDto);
    }

    /**
     * Get all the deviceTelemetries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceTelemetryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceTelemetries");
        return deviceTelemetryRepository.findAll(pageable).map(deviceTelemetryMapper::toDto);
    }

    /**
     * Get one deviceTelemetry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceTelemetryDTO> findOne(Long id) {
        log.debug("Request to get DeviceTelemetry : {}", id);
        return deviceTelemetryRepository.findById(id).map(deviceTelemetryMapper::toDto);
    }

    /**
     * Delete the deviceTelemetry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeviceTelemetry : {}", id);
        deviceTelemetryRepository.deleteById(id);
    }
}
