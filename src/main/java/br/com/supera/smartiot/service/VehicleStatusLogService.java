package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleStatusLog;
import br.com.supera.smartiot.repository.VehicleStatusLogRepository;
import br.com.supera.smartiot.service.dto.VehicleStatusLogDTO;
import br.com.supera.smartiot.service.mapper.VehicleStatusLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleStatusLog}.
 */
@Service
@Transactional
public class VehicleStatusLogService {

    private final Logger log = LoggerFactory.getLogger(VehicleStatusLogService.class);

    private final VehicleStatusLogRepository vehicleStatusLogRepository;

    private final VehicleStatusLogMapper vehicleStatusLogMapper;

    public VehicleStatusLogService(VehicleStatusLogRepository vehicleStatusLogRepository, VehicleStatusLogMapper vehicleStatusLogMapper) {
        this.vehicleStatusLogRepository = vehicleStatusLogRepository;
        this.vehicleStatusLogMapper = vehicleStatusLogMapper;
    }

    /**
     * Save a vehicleStatusLog.
     *
     * @param vehicleStatusLogDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleStatusLogDTO save(VehicleStatusLogDTO vehicleStatusLogDTO) {
        log.debug("Request to save VehicleStatusLog : {}", vehicleStatusLogDTO);
        VehicleStatusLog vehicleStatusLog = vehicleStatusLogMapper.toEntity(vehicleStatusLogDTO);
        vehicleStatusLog = vehicleStatusLogRepository.save(vehicleStatusLog);
        return vehicleStatusLogMapper.toDto(vehicleStatusLog);
    }

    /**
     * Update a vehicleStatusLog.
     *
     * @param vehicleStatusLogDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleStatusLogDTO update(VehicleStatusLogDTO vehicleStatusLogDTO) {
        log.debug("Request to update VehicleStatusLog : {}", vehicleStatusLogDTO);
        VehicleStatusLog vehicleStatusLog = vehicleStatusLogMapper.toEntity(vehicleStatusLogDTO);
        vehicleStatusLog = vehicleStatusLogRepository.save(vehicleStatusLog);
        return vehicleStatusLogMapper.toDto(vehicleStatusLog);
    }

    /**
     * Partially update a vehicleStatusLog.
     *
     * @param vehicleStatusLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleStatusLogDTO> partialUpdate(VehicleStatusLogDTO vehicleStatusLogDTO) {
        log.debug("Request to partially update VehicleStatusLog : {}", vehicleStatusLogDTO);

        return vehicleStatusLogRepository
            .findById(vehicleStatusLogDTO.getId())
            .map(existingVehicleStatusLog -> {
                vehicleStatusLogMapper.partialUpdate(existingVehicleStatusLog, vehicleStatusLogDTO);

                return existingVehicleStatusLog;
            })
            .map(vehicleStatusLogRepository::save)
            .map(vehicleStatusLogMapper::toDto);
    }

    /**
     * Get all the vehicleStatusLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleStatusLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleStatusLogs");
        return vehicleStatusLogRepository.findAll(pageable).map(vehicleStatusLogMapper::toDto);
    }

    /**
     * Get one vehicleStatusLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleStatusLogDTO> findOne(Long id) {
        log.debug("Request to get VehicleStatusLog : {}", id);
        return vehicleStatusLogRepository.findById(id).map(vehicleStatusLogMapper::toDto);
    }

    /**
     * Delete the vehicleStatusLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleStatusLog : {}", id);
        vehicleStatusLogRepository.deleteById(id);
    }
}
