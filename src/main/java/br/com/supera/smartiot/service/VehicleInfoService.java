package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.VehicleInfo;
import br.com.supera.smartiot.repository.VehicleInfoRepository;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
import br.com.supera.smartiot.service.mapper.VehicleInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.VehicleInfo}.
 */
@Service
@Transactional
public class VehicleInfoService {

    private final Logger log = LoggerFactory.getLogger(VehicleInfoService.class);

    private final VehicleInfoRepository vehicleInfoRepository;

    private final VehicleInfoMapper vehicleInfoMapper;

    public VehicleInfoService(VehicleInfoRepository vehicleInfoRepository, VehicleInfoMapper vehicleInfoMapper) {
        this.vehicleInfoRepository = vehicleInfoRepository;
        this.vehicleInfoMapper = vehicleInfoMapper;
    }

    /**
     * Save a vehicleInfo.
     *
     * @param vehicleInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleInfoDTO save(VehicleInfoDTO vehicleInfoDTO) {
        log.debug("Request to save VehicleInfo : {}", vehicleInfoDTO);
        VehicleInfo vehicleInfo = vehicleInfoMapper.toEntity(vehicleInfoDTO);
        vehicleInfo = vehicleInfoRepository.save(vehicleInfo);
        return vehicleInfoMapper.toDto(vehicleInfo);
    }

    /**
     * Update a vehicleInfo.
     *
     * @param vehicleInfoDTO the entity to save.
     * @return the persisted entity.
     */
    public VehicleInfoDTO update(VehicleInfoDTO vehicleInfoDTO) {
        log.debug("Request to update VehicleInfo : {}", vehicleInfoDTO);
        VehicleInfo vehicleInfo = vehicleInfoMapper.toEntity(vehicleInfoDTO);
        vehicleInfo = vehicleInfoRepository.save(vehicleInfo);
        return vehicleInfoMapper.toDto(vehicleInfo);
    }

    /**
     * Partially update a vehicleInfo.
     *
     * @param vehicleInfoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<VehicleInfoDTO> partialUpdate(VehicleInfoDTO vehicleInfoDTO) {
        log.debug("Request to partially update VehicleInfo : {}", vehicleInfoDTO);

        return vehicleInfoRepository
            .findById(vehicleInfoDTO.getId())
            .map(existingVehicleInfo -> {
                vehicleInfoMapper.partialUpdate(existingVehicleInfo, vehicleInfoDTO);

                return existingVehicleInfo;
            })
            .map(vehicleInfoRepository::save)
            .map(vehicleInfoMapper::toDto);
    }

    /**
     * Get all the vehicleInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleInfos");
        return vehicleInfoRepository.findAll(pageable).map(vehicleInfoMapper::toDto);
    }

    /**
     * Get one vehicleInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VehicleInfoDTO> findOne(Long id) {
        log.debug("Request to get VehicleInfo : {}", id);
        return vehicleInfoRepository.findById(id).map(vehicleInfoMapper::toDto);
    }

    /**
     * Delete the vehicleInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VehicleInfo : {}", id);
        vehicleInfoRepository.deleteById(id);
    }
}
