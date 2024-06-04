package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleGroup;
import br.com.supera.smartiot.repository.VehicleGroupRepository;
import br.com.supera.smartiot.service.dto.VehicleGroupDTO;
import br.com.supera.smartiot.service.mapper.VehicleGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleGroup}.
 */
@Service
@Transactional
public class VehicleGroupService {

    private final Logger log = LoggerFactory.getLogger(VehicleGroupService.class);

    private final VehicleGroupRepository vehicleGroupRepository;

    private final VehicleGroupMapper vehicleGroupMapper;

    public VehicleGroupService(VehicleGroupRepository vehicleGroupRepository, VehicleGroupMapper vehicleGroupMapper) {
        this.vehicleGroupRepository = vehicleGroupRepository;
        this.vehicleGroupMapper = vehicleGroupMapper;
    }

    /**
     * Save a vehicleGroup.
     *
     * @param vehicleGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleGroupDTO save(VehicleGroupDTO vehicleGroupDTO) {
        log.debug("Request to save VehicleGroup : {}", vehicleGroupDTO);
        VehicleGroup vehicleGroup = vehicleGroupMapper.toEntity(vehicleGroupDTO);
        vehicleGroup = vehicleGroupRepository.save(vehicleGroup);
        return vehicleGroupMapper.toDto(vehicleGroup);
    }

    /**
     * Update a vehicleGroup.
     *
     * @param vehicleGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleGroupDTO update(VehicleGroupDTO vehicleGroupDTO) {
        log.debug("Request to update VehicleGroup : {}", vehicleGroupDTO);
        VehicleGroup vehicleGroup = vehicleGroupMapper.toEntity(vehicleGroupDTO);
        vehicleGroup = vehicleGroupRepository.save(vehicleGroup);
        return vehicleGroupMapper.toDto(vehicleGroup);
    }

    /**
     * Partially update a vehicleGroup.
     *
     * @param vehicleGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleGroupDTO> partialUpdate(VehicleGroupDTO vehicleGroupDTO) {
        log.debug("Request to partially update VehicleGroup : {}", vehicleGroupDTO);

        return vehicleGroupRepository
            .findById(vehicleGroupDTO.getId())
            .map(existingVehicleGroup -> {
                vehicleGroupMapper.partialUpdate(existingVehicleGroup, vehicleGroupDTO);

                return existingVehicleGroup;
            })
            .map(vehicleGroupRepository::save)
            .map(vehicleGroupMapper::toDto);
    }

    /**
     * Get all the vehicleGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleGroups");
        return vehicleGroupRepository.findAll(pageable).map(vehicleGroupMapper::toDto);
    }

    /**
     * Get one vehicleGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleGroupDTO> findOne(Long id) {
        log.debug("Request to get VehicleGroup : {}", id);
        return vehicleGroupRepository.findById(id).map(vehicleGroupMapper::toDto);
    }

    /**
     * Delete the vehicleGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleGroup : {}", id);
        vehicleGroupRepository.deleteById(id);
    }
}
