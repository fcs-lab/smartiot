package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.WaterUsageLog;
import br.com.supera.smartiot.repository.WaterUsageLogRepository;
import br.com.supera.smartiot.service.dto.WaterUsageLogDTO;
import br.com.supera.smartiot.service.mapper.WaterUsageLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.WaterUsageLog}.
 */
@Service
@Transactional
public class WaterUsageLogService {

    private final Logger log = LoggerFactory.getLogger(WaterUsageLogService.class);

    private final WaterUsageLogRepository waterUsageLogRepository;

    private final WaterUsageLogMapper waterUsageLogMapper;

    public WaterUsageLogService(WaterUsageLogRepository waterUsageLogRepository, WaterUsageLogMapper waterUsageLogMapper) {
        this.waterUsageLogRepository = waterUsageLogRepository;
        this.waterUsageLogMapper = waterUsageLogMapper;
    }

    /**
     * Save a waterUsageLog.
     *
     * @param waterUsageLogDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterUsageLogDTO save(WaterUsageLogDTO waterUsageLogDTO) {
        log.debug("Request to save WaterUsageLog : {}", waterUsageLogDTO);
        WaterUsageLog waterUsageLog = waterUsageLogMapper.toEntity(waterUsageLogDTO);
        waterUsageLog = waterUsageLogRepository.save(waterUsageLog);
        return waterUsageLogMapper.toDto(waterUsageLog);
    }

    /**
     * Update a waterUsageLog.
     *
     * @param waterUsageLogDTO the entity to save.
     * @return the persisted entity.
     */
    public WaterUsageLogDTO update(WaterUsageLogDTO waterUsageLogDTO) {
        log.debug("Request to update WaterUsageLog : {}", waterUsageLogDTO);
        WaterUsageLog waterUsageLog = waterUsageLogMapper.toEntity(waterUsageLogDTO);
        waterUsageLog = waterUsageLogRepository.save(waterUsageLog);
        return waterUsageLogMapper.toDto(waterUsageLog);
    }

    /**
     * Partially update a waterUsageLog.
     *
     * @param waterUsageLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WaterUsageLogDTO> partialUpdate(WaterUsageLogDTO waterUsageLogDTO) {
        log.debug("Request to partially update WaterUsageLog : {}", waterUsageLogDTO);

        return waterUsageLogRepository
            .findById(waterUsageLogDTO.getId())
            .map(existingWaterUsageLog -> {
                waterUsageLogMapper.partialUpdate(existingWaterUsageLog, waterUsageLogDTO);

                return existingWaterUsageLog;
            })
            .map(waterUsageLogRepository::save)
            .map(waterUsageLogMapper::toDto);
    }

    /**
     * Get all the waterUsageLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WaterUsageLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WaterUsageLogs");
        return waterUsageLogRepository.findAll(pageable).map(waterUsageLogMapper::toDto);
    }

    /**
     * Get one waterUsageLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WaterUsageLogDTO> findOne(Long id) {
        log.debug("Request to get WaterUsageLog : {}", id);
        return waterUsageLogRepository.findById(id).map(waterUsageLogMapper::toDto);
    }

    /**
     * Delete the waterUsageLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WaterUsageLog : {}", id);
        waterUsageLogRepository.deleteById(id);
    }
}
