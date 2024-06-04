package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleManufacturer;
import br.com.supera.smartiot.repository.VehicleManufacturerRepository;
import br.com.supera.smartiot.service.dto.VehicleManufacturerDTO;
import br.com.supera.smartiot.service.mapper.VehicleManufacturerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleManufacturer}.
 */
@Service
@Transactional
public class VehicleManufacturerService {

    private final Logger log = LoggerFactory.getLogger(VehicleManufacturerService.class);

    private final VehicleManufacturerRepository vehicleManufacturerRepository;

    private final VehicleManufacturerMapper vehicleManufacturerMapper;

    public VehicleManufacturerService(
        VehicleManufacturerRepository vehicleManufacturerRepository,
        VehicleManufacturerMapper vehicleManufacturerMapper
    ) {
        this.vehicleManufacturerRepository = vehicleManufacturerRepository;
        this.vehicleManufacturerMapper = vehicleManufacturerMapper;
    }

    /**
     * Save a vehicleManufacturer.
     *
     * @param vehicleManufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleManufacturerDTO save(VehicleManufacturerDTO vehicleManufacturerDTO) {
        log.debug("Request to save VehicleManufacturer : {}", vehicleManufacturerDTO);
        VehicleManufacturer vehicleManufacturer = vehicleManufacturerMapper.toEntity(vehicleManufacturerDTO);
        vehicleManufacturer = vehicleManufacturerRepository.save(vehicleManufacturer);
        return vehicleManufacturerMapper.toDto(vehicleManufacturer);
    }

    /**
     * Update a vehicleManufacturer.
     *
     * @param vehicleManufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleManufacturerDTO update(VehicleManufacturerDTO vehicleManufacturerDTO) {
        log.debug("Request to update VehicleManufacturer : {}", vehicleManufacturerDTO);
        VehicleManufacturer vehicleManufacturer = vehicleManufacturerMapper.toEntity(vehicleManufacturerDTO);
        vehicleManufacturer = vehicleManufacturerRepository.save(vehicleManufacturer);
        return vehicleManufacturerMapper.toDto(vehicleManufacturer);
    }

    /**
     * Partially update a vehicleManufacturer.
     *
     * @param vehicleManufacturerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleManufacturerDTO> partialUpdate(VehicleManufacturerDTO vehicleManufacturerDTO) {
        log.debug("Request to partially update VehicleManufacturer : {}", vehicleManufacturerDTO);

        return vehicleManufacturerRepository
            .findById(vehicleManufacturerDTO.getId())
            .map(existingVehicleManufacturer -> {
                vehicleManufacturerMapper.partialUpdate(existingVehicleManufacturer, vehicleManufacturerDTO);

                return existingVehicleManufacturer;
            })
            .map(vehicleManufacturerRepository::save)
            .map(vehicleManufacturerMapper::toDto);
    }

    /**
     * Get all the vehicleManufacturers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleManufacturerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleManufacturers");
        return vehicleManufacturerRepository.findAll(pageable).map(vehicleManufacturerMapper::toDto);
    }

    /**
     * Get one vehicleManufacturer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleManufacturerDTO> findOne(Long id) {
        log.debug("Request to get VehicleManufacturer : {}", id);
        return vehicleManufacturerRepository.findById(id).map(vehicleManufacturerMapper::toDto);
    }

    /**
     * Delete the vehicleManufacturer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleManufacturer : {}", id);
        vehicleManufacturerRepository.deleteById(id);
    }
}
