package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleDamage;
import br.com.supera.smartiot.repository.VehicleDamageRepository;
import br.com.supera.smartiot.service.dto.VehicleDamageDTO;
import br.com.supera.smartiot.service.mapper.VehicleDamageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleDamage}.
 */
@Service
@Transactional
public class VehicleDamageService {

    private final Logger log = LoggerFactory.getLogger(VehicleDamageService.class);

    private final VehicleDamageRepository vehicleDamageRepository;

    private final VehicleDamageMapper vehicleDamageMapper;

    public VehicleDamageService(VehicleDamageRepository vehicleDamageRepository, VehicleDamageMapper vehicleDamageMapper) {
        this.vehicleDamageRepository = vehicleDamageRepository;
        this.vehicleDamageMapper = vehicleDamageMapper;
    }

    /**
     * Save a vehicleDamage.
     *
     * @param vehicleDamageDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDamageDTO save(VehicleDamageDTO vehicleDamageDTO) {
        log.debug("Request to save VehicleDamage : {}", vehicleDamageDTO);
        VehicleDamage vehicleDamage = vehicleDamageMapper.toEntity(vehicleDamageDTO);
        vehicleDamage = vehicleDamageRepository.save(vehicleDamage);
        return vehicleDamageMapper.toDto(vehicleDamage);
    }

    /**
     * Update a vehicleDamage.
     *
     * @param vehicleDamageDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleDamageDTO update(VehicleDamageDTO vehicleDamageDTO) {
        log.debug("Request to update VehicleDamage : {}", vehicleDamageDTO);
        VehicleDamage vehicleDamage = vehicleDamageMapper.toEntity(vehicleDamageDTO);
        vehicleDamage = vehicleDamageRepository.save(vehicleDamage);
        return vehicleDamageMapper.toDto(vehicleDamage);
    }

    /**
     * Partially update a vehicleDamage.
     *
     * @param vehicleDamageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleDamageDTO> partialUpdate(VehicleDamageDTO vehicleDamageDTO) {
        log.debug("Request to partially update VehicleDamage : {}", vehicleDamageDTO);

        return vehicleDamageRepository
            .findById(vehicleDamageDTO.getId())
            .map(existingVehicleDamage -> {
                vehicleDamageMapper.partialUpdate(existingVehicleDamage, vehicleDamageDTO);

                return existingVehicleDamage;
            })
            .map(vehicleDamageRepository::save)
            .map(vehicleDamageMapper::toDto);
    }

    /**
     * Get all the vehicleDamages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDamageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleDamages");
        return vehicleDamageRepository.findAll(pageable).map(vehicleDamageMapper::toDto);
    }

    /**
     * Get one vehicleDamage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleDamageDTO> findOne(Long id) {
        log.debug("Request to get VehicleDamage : {}", id);
        return vehicleDamageRepository.findById(id).map(vehicleDamageMapper::toDto);
    }

    /**
     * Delete the vehicleDamage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleDamage : {}", id);
        vehicleDamageRepository.deleteById(id);
    }
}
