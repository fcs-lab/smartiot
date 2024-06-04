package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleModel;
import br.com.supera.smartiot.repository.VehicleModelRepository;
import br.com.supera.smartiot.service.dto.VehicleModelDTO;
import br.com.supera.smartiot.service.mapper.VehicleModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleModel}.
 */
@Service
@Transactional
public class VehicleModelService {

    private final Logger log = LoggerFactory.getLogger(VehicleModelService.class);

    private final VehicleModelRepository vehicleModelRepository;

    private final VehicleModelMapper vehicleModelMapper;

    public VehicleModelService(VehicleModelRepository vehicleModelRepository, VehicleModelMapper vehicleModelMapper) {
        this.vehicleModelRepository = vehicleModelRepository;
        this.vehicleModelMapper = vehicleModelMapper;
    }

    /**
     * Save a vehicleModel.
     *
     * @param vehicleModelDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleModelDTO save(VehicleModelDTO vehicleModelDTO) {
        log.debug("Request to save VehicleModel : {}", vehicleModelDTO);
        VehicleModel vehicleModel = vehicleModelMapper.toEntity(vehicleModelDTO);
        vehicleModel = vehicleModelRepository.save(vehicleModel);
        return vehicleModelMapper.toDto(vehicleModel);
    }

    /**
     * Update a vehicleModel.
     *
     * @param vehicleModelDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleModelDTO update(VehicleModelDTO vehicleModelDTO) {
        log.debug("Request to update VehicleModel : {}", vehicleModelDTO);
        VehicleModel vehicleModel = vehicleModelMapper.toEntity(vehicleModelDTO);
        vehicleModel = vehicleModelRepository.save(vehicleModel);
        return vehicleModelMapper.toDto(vehicleModel);
    }

    /**
     * Partially update a vehicleModel.
     *
     * @param vehicleModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleModelDTO> partialUpdate(VehicleModelDTO vehicleModelDTO) {
        log.debug("Request to partially update VehicleModel : {}", vehicleModelDTO);

        return vehicleModelRepository
            .findById(vehicleModelDTO.getId())
            .map(existingVehicleModel -> {
                vehicleModelMapper.partialUpdate(existingVehicleModel, vehicleModelDTO);

                return existingVehicleModel;
            })
            .map(vehicleModelRepository::save)
            .map(vehicleModelMapper::toDto);
    }

    /**
     * Get all the vehicleModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleModels");
        return vehicleModelRepository.findAll(pageable).map(vehicleModelMapper::toDto);
    }

    /**
     * Get one vehicleModel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleModelDTO> findOne(Long id) {
        log.debug("Request to get VehicleModel : {}", id);
        return vehicleModelRepository.findById(id).map(vehicleModelMapper::toDto);
    }

    /**
     * Delete the vehicleModel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleModel : {}", id);
        vehicleModelRepository.deleteById(id);
    }
}
