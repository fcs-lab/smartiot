package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.DeviceCommand;
import br.com.supera.smartiot.repository.DeviceCommandRepository;
import br.com.supera.smartiot.service.dto.DeviceCommandDTO;
import br.com.supera.smartiot.service.mapper.DeviceCommandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.DeviceCommand}.
 */
@Service
@Transactional
public class DeviceCommandService {

    private final Logger log = LoggerFactory.getLogger(DeviceCommandService.class);

    private final DeviceCommandRepository deviceCommandRepository;

    private final DeviceCommandMapper deviceCommandMapper;

    public DeviceCommandService(DeviceCommandRepository deviceCommandRepository, DeviceCommandMapper deviceCommandMapper) {
        this.deviceCommandRepository = deviceCommandRepository;
        this.deviceCommandMapper = deviceCommandMapper;
    }

    /**
     * Save a deviceCommand.
     *
     * @param deviceCommandDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceCommandDTO save(DeviceCommandDTO deviceCommandDTO) {
        log.debug("Request to save DeviceCommand : {}", deviceCommandDTO);
        DeviceCommand deviceCommand = deviceCommandMapper.toEntity(deviceCommandDTO);
        deviceCommand = deviceCommandRepository.save(deviceCommand);
        return deviceCommandMapper.toDto(deviceCommand);
    }

    /**
     * Update a deviceCommand.
     *
     * @param deviceCommandDTO the entity to save.
     * @return the persisted entity.
     */
    public DeviceCommandDTO update(DeviceCommandDTO deviceCommandDTO) {
        log.debug("Request to update DeviceCommand : {}", deviceCommandDTO);
        DeviceCommand deviceCommand = deviceCommandMapper.toEntity(deviceCommandDTO);
        deviceCommand = deviceCommandRepository.save(deviceCommand);
        return deviceCommandMapper.toDto(deviceCommand);
    }

    /**
     * Partially update a deviceCommand.
     *
     * @param deviceCommandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeviceCommandDTO> partialUpdate(DeviceCommandDTO deviceCommandDTO) {
        log.debug("Request to partially update DeviceCommand : {}", deviceCommandDTO);

        return deviceCommandRepository
            .findById(deviceCommandDTO.getId())
            .map(existingDeviceCommand -> {
                deviceCommandMapper.partialUpdate(existingDeviceCommand, deviceCommandDTO);

                return existingDeviceCommand;
            })
            .map(deviceCommandRepository::save)
            .map(deviceCommandMapper::toDto);
    }

    /**
     * Get all the deviceCommands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceCommandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceCommands");
        return deviceCommandRepository.findAll(pageable).map(deviceCommandMapper::toDto);
    }

    /**
     * Get one deviceCommand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeviceCommandDTO> findOne(Long id) {
        log.debug("Request to get DeviceCommand : {}", id);
        return deviceCommandRepository.findById(id).map(deviceCommandMapper::toDto);
    }

    /**
     * Delete the deviceCommand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DeviceCommand : {}", id);
        deviceCommandRepository.deleteById(id);
    }
}
