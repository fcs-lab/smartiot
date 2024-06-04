package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleService;
import br.com.supera.smartiot.repository.VehicleServiceRepository;
import br.com.supera.smartiot.service.dto.VehicleServiceDTO;
import br.com.supera.smartiot.service.mapper.VehicleServiceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleService}.
 */
@Service
@Transactional
public class VehicleServiceService {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceService.class);

    private final VehicleServiceRepository vehicleServiceRepository;

    private final VehicleServiceMapper vehicleServiceMapper;

    public VehicleServiceService(VehicleServiceRepository vehicleServiceRepository, VehicleServiceMapper vehicleServiceMapper) {
        this.vehicleServiceRepository = vehicleServiceRepository;
        this.vehicleServiceMapper = vehicleServiceMapper;
    }

    /**
     * Save a vehicleService.
     *
     * @param vehicleServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleServiceDTO save(VehicleServiceDTO vehicleServiceDTO) {
        log.debug("Request to save VehicleService : {}", vehicleServiceDTO);
        VehicleService vehicleService = vehicleServiceMapper.toEntity(vehicleServiceDTO);
        vehicleService = vehicleServiceRepository.save(vehicleService);
        return vehicleServiceMapper.toDto(vehicleService);
    }

    /**
     * Update a vehicleService.
     *
     * @param vehicleServiceDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleServiceDTO update(VehicleServiceDTO vehicleServiceDTO) {
        log.debug("Request to update VehicleService : {}", vehicleServiceDTO);
        VehicleService vehicleService = vehicleServiceMapper.toEntity(vehicleServiceDTO);
        vehicleService = vehicleServiceRepository.save(vehicleService);
        return vehicleServiceMapper.toDto(vehicleService);
    }

    /**
     * Partially update a vehicleService.
     *
     * @param vehicleServiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleServiceDTO> partialUpdate(VehicleServiceDTO vehicleServiceDTO) {
        log.debug("Request to partially update VehicleService : {}", vehicleServiceDTO);

        return vehicleServiceRepository
            .findById(vehicleServiceDTO.getId())
            .map(existingVehicleService -> {
                vehicleServiceMapper.partialUpdate(existingVehicleService, vehicleServiceDTO);

                return existingVehicleService;
            })
            .map(vehicleServiceRepository::save)
            .map(vehicleServiceMapper::toDto);
    }

    /**
     * Get all the vehicleServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleServiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleServices");
        return vehicleServiceRepository.findAll(pageable).map(vehicleServiceMapper::toDto);
    }

    /**
     * Get one vehicleService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleServiceDTO> findOne(Long id) {
        log.debug("Request to get VehicleService : {}", id);
        return vehicleServiceRepository.findById(id).map(vehicleServiceMapper::toDto);
    }

    /**
     * Delete the vehicleService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleService : {}", id);
        vehicleServiceRepository.deleteById(id);
    }
}
