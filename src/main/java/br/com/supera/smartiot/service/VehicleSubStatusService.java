package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleSubStatus;
import br.com.supera.smartiot.repository.VehicleSubStatusRepository;
import br.com.supera.smartiot.service.dto.VehicleSubStatusDTO;
import br.com.supera.smartiot.service.mapper.VehicleSubStatusMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleSubStatus}.
 */
@Service
@Transactional
public class VehicleSubStatusService {

    private final Logger log = LoggerFactory.getLogger(VehicleSubStatusService.class);

    private final VehicleSubStatusRepository vehicleSubStatusRepository;

    private final VehicleSubStatusMapper vehicleSubStatusMapper;

    public VehicleSubStatusService(VehicleSubStatusRepository vehicleSubStatusRepository, VehicleSubStatusMapper vehicleSubStatusMapper) {
        this.vehicleSubStatusRepository = vehicleSubStatusRepository;
        this.vehicleSubStatusMapper = vehicleSubStatusMapper;
    }

    /**
     * Save a vehicleSubStatus.
     *
     * @param vehicleSubStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleSubStatusDTO save(VehicleSubStatusDTO vehicleSubStatusDTO) {
        log.debug("Request to save VehicleSubStatus : {}", vehicleSubStatusDTO);
        VehicleSubStatus vehicleSubStatus = vehicleSubStatusMapper.toEntity(vehicleSubStatusDTO);
        vehicleSubStatus = vehicleSubStatusRepository.save(vehicleSubStatus);
        return vehicleSubStatusMapper.toDto(vehicleSubStatus);
    }

    /**
     * Update a vehicleSubStatus.
     *
     * @param vehicleSubStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleSubStatusDTO update(VehicleSubStatusDTO vehicleSubStatusDTO) {
        log.debug("Request to update VehicleSubStatus : {}", vehicleSubStatusDTO);
        VehicleSubStatus vehicleSubStatus = vehicleSubStatusMapper.toEntity(vehicleSubStatusDTO);
        vehicleSubStatus = vehicleSubStatusRepository.save(vehicleSubStatus);
        return vehicleSubStatusMapper.toDto(vehicleSubStatus);
    }

    /**
     * Partially update a vehicleSubStatus.
     *
     * @param vehicleSubStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleSubStatusDTO> partialUpdate(VehicleSubStatusDTO vehicleSubStatusDTO) {
        log.debug("Request to partially update VehicleSubStatus : {}", vehicleSubStatusDTO);

        return vehicleSubStatusRepository
            .findById(vehicleSubStatusDTO.getId())
            .map(existingVehicleSubStatus -> {
                vehicleSubStatusMapper.partialUpdate(existingVehicleSubStatus, vehicleSubStatusDTO);

                return existingVehicleSubStatus;
            })
            .map(vehicleSubStatusRepository::save)
            .map(vehicleSubStatusMapper::toDto);
    }

    /**
     * Get all the vehicleSubStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleSubStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleSubStatuses");
        return vehicleSubStatusRepository.findAll(pageable).map(vehicleSubStatusMapper::toDto);
    }

    /**
     * Get one vehicleSubStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleSubStatusDTO> findOne(Long id) {
        log.debug("Request to get VehicleSubStatus : {}", id);
        return vehicleSubStatusRepository.findById(id).map(vehicleSubStatusMapper::toDto);
    }

    /**
     * Delete the vehicleSubStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleSubStatus : {}", id);
        vehicleSubStatusRepository.deleteById(id);
    }
}
