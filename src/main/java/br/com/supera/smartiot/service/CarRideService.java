package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.CarRide;
import br.com.supera.smartiot.repository.CarRideRepository;
import br.com.supera.smartiot.service.dto.CarRideDTO;
import br.com.supera.smartiot.service.mapper.CarRideMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.CarRide}.
 */
@Service
@Transactional
public class CarRideService {

    private final Logger log = LoggerFactory.getLogger(CarRideService.class);

    private final CarRideRepository carRideRepository;

    private final CarRideMapper carRideMapper;

    public CarRideService(CarRideRepository carRideRepository, CarRideMapper carRideMapper) {
        this.carRideRepository = carRideRepository;
        this.carRideMapper = carRideMapper;
    }

    /**
     * Save a carRide.
     *
     * @param carRideDTO the entity to save.
     * @return the persisted entity.
     */
    public CarRideDTO save(CarRideDTO carRideDTO) {
        log.debug("Request to save CarRide : {}", carRideDTO);
        CarRide carRide = carRideMapper.toEntity(carRideDTO);
        carRide = carRideRepository.save(carRide);
        return carRideMapper.toDto(carRide);
    }

    /**
     * Update a carRide.
     *
     * @param carRideDTO the entity to save.
     * @return the persisted entity.
     */
    public CarRideDTO update(CarRideDTO carRideDTO) {
        log.debug("Request to update CarRide : {}", carRideDTO);
        CarRide carRide = carRideMapper.toEntity(carRideDTO);
        carRide = carRideRepository.save(carRide);
        return carRideMapper.toDto(carRide);
    }

    /**
     * Partially update a carRide.
     *
     * @param carRideDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CarRideDTO> partialUpdate(CarRideDTO carRideDTO) {
        log.debug("Request to partially update CarRide : {}", carRideDTO);

        return carRideRepository
            .findById(carRideDTO.getId())
            .map(existingCarRide -> {
                carRideMapper.partialUpdate(existingCarRide, carRideDTO);

                return existingCarRide;
            })
            .map(carRideRepository::save)
            .map(carRideMapper::toDto);
    }

    /**
     * Get all the carRides.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CarRideDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CarRides");
        return carRideRepository.findAll(pageable).map(carRideMapper::toDto);
    }

    /**
     * Get one carRide by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarRideDTO> findOne(Long id) {
        log.debug("Request to get CarRide : {}", id);
        return carRideRepository.findById(id).map(carRideMapper::toDto);
    }

    /**
     * Delete the carRide by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CarRide : {}", id);
        carRideRepository.deleteById(id);
    }
}
