package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.AppDevice;
import br.com.supera.smartiot.repository.AppDeviceRepository;
import br.com.supera.smartiot.service.dto.AppDeviceDTO;
import br.com.supera.smartiot.service.mapper.AppDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.AppDevice}.
 */
@Service
@Transactional
public class AppDeviceService {

    private final Logger log = LoggerFactory.getLogger(AppDeviceService.class);

    private final AppDeviceRepository appDeviceRepository;

    private final AppDeviceMapper appDeviceMapper;

    public AppDeviceService(AppDeviceRepository appDeviceRepository, AppDeviceMapper appDeviceMapper) {
        this.appDeviceRepository = appDeviceRepository;
        this.appDeviceMapper = appDeviceMapper;
    }

    /**
     * Save a appDevice.
     *
     * @param appDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    public AppDeviceDTO save(AppDeviceDTO appDeviceDTO) {
        log.debug("Request to save AppDevice : {}", appDeviceDTO);
        AppDevice appDevice = appDeviceMapper.toEntity(appDeviceDTO);
        appDevice = appDeviceRepository.save(appDevice);
        return appDeviceMapper.toDto(appDevice);
    }

    /**
     * Update a appDevice.
     *
     * @param appDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    public AppDeviceDTO update(AppDeviceDTO appDeviceDTO) {
        log.debug("Request to update AppDevice : {}", appDeviceDTO);
        AppDevice appDevice = appDeviceMapper.toEntity(appDeviceDTO);
        appDevice = appDeviceRepository.save(appDevice);
        return appDeviceMapper.toDto(appDevice);
    }

    /**
     * Partially update a appDevice.
     *
     * @param appDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppDeviceDTO> partialUpdate(AppDeviceDTO appDeviceDTO) {
        log.debug("Request to partially update AppDevice : {}", appDeviceDTO);

        return appDeviceRepository
            .findById(appDeviceDTO.getId())
            .map(existingAppDevice -> {
                appDeviceMapper.partialUpdate(existingAppDevice, appDeviceDTO);

                return existingAppDevice;
            })
            .map(appDeviceRepository::save)
            .map(appDeviceMapper::toDto);
    }

    /**
     * Get all the appDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppDevices");
        return appDeviceRepository.findAll(pageable).map(appDeviceMapper::toDto);
    }

    /**
     * Get one appDevice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppDeviceDTO> findOne(Long id) {
        log.debug("Request to get AppDevice : {}", id);
        return appDeviceRepository.findById(id).map(appDeviceMapper::toDto);
    }

    /**
     * Delete the appDevice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppDevice : {}", id);
        appDeviceRepository.deleteById(id);
    }
}
