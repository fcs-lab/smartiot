package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.GeoLocation;
import br.com.supera.smartiot.repository.GeoLocationRepository;
import br.com.supera.smartiot.service.dto.GeoLocationDTO;
import br.com.supera.smartiot.service.mapper.GeoLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.GeoLocation}.
 */
@Service
@Transactional
public class GeoLocationService {

    private final Logger log = LoggerFactory.getLogger(GeoLocationService.class);

    private final GeoLocationRepository geoLocationRepository;

    private final GeoLocationMapper geoLocationMapper;

    public GeoLocationService(GeoLocationRepository geoLocationRepository, GeoLocationMapper geoLocationMapper) {
        this.geoLocationRepository = geoLocationRepository;
        this.geoLocationMapper = geoLocationMapper;
    }

    /**
     * Save a geoLocation.
     *
     * @param geoLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public GeoLocationDTO save(GeoLocationDTO geoLocationDTO) {
        log.debug("Request to save GeoLocation : {}", geoLocationDTO);
        GeoLocation geoLocation = geoLocationMapper.toEntity(geoLocationDTO);
        geoLocation = geoLocationRepository.save(geoLocation);
        return geoLocationMapper.toDto(geoLocation);
    }

    /**
     * Update a geoLocation.
     *
     * @param geoLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public GeoLocationDTO update(GeoLocationDTO geoLocationDTO) {
        log.debug("Request to update GeoLocation : {}", geoLocationDTO);
        GeoLocation geoLocation = geoLocationMapper.toEntity(geoLocationDTO);
        geoLocation = geoLocationRepository.save(geoLocation);
        return geoLocationMapper.toDto(geoLocation);
    }

    /**
     * Partially update a geoLocation.
     *
     * @param geoLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GeoLocationDTO> partialUpdate(GeoLocationDTO geoLocationDTO) {
        log.debug("Request to partially update GeoLocation : {}", geoLocationDTO);

        return geoLocationRepository
            .findById(geoLocationDTO.getId())
            .map(existingGeoLocation -> {
                geoLocationMapper.partialUpdate(existingGeoLocation, geoLocationDTO);

                return existingGeoLocation;
            })
            .map(geoLocationRepository::save)
            .map(geoLocationMapper::toDto);
    }

    /**
     * Get all the geoLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GeoLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GeoLocations");
        return geoLocationRepository.findAll(pageable).map(geoLocationMapper::toDto);
    }

    /**
     * Get one geoLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeoLocationDTO> findOne(Long id) {
        log.debug("Request to get GeoLocation : {}", id);
        return geoLocationRepository.findById(id).map(geoLocationMapper::toDto);
    }

    /**
     * Delete the geoLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GeoLocation : {}", id);
        geoLocationRepository.deleteById(id);
    }
}
