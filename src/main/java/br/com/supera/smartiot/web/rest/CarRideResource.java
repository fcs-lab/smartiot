package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.CarRideRepository;
import br.com.supera.smartiot.service.CarRideService;
import br.com.supera.smartiot.service.dto.CarRideDTO;
import br.com.supera.smartiot.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.supera.smartiot.domain.CarRide}.
 */
@RestController
@RequestMapping("/api/car-rides")
public class CarRideResource {

    private final Logger log = LoggerFactory.getLogger(CarRideResource.class);

    private static final String ENTITY_NAME = "carRide";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarRideService carRideService;

    private final CarRideRepository carRideRepository;

    public CarRideResource(CarRideService carRideService, CarRideRepository carRideRepository) {
        this.carRideService = carRideService;
        this.carRideRepository = carRideRepository;
    }

    /**
     * {@code POST  /car-rides} : Create a new carRide.
     *
     * @param carRideDTO the carRideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carRideDTO, or with status {@code 400 (Bad Request)} if the carRide has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarRideDTO> createCarRide(@Valid @RequestBody CarRideDTO carRideDTO) throws URISyntaxException {
        log.debug("REST request to save CarRide : {}", carRideDTO);
        if (carRideDTO.getId() != null) {
            throw new BadRequestAlertException("A new carRide cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carRideDTO = carRideService.save(carRideDTO);
        return ResponseEntity.created(new URI("/api/car-rides/" + carRideDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carRideDTO.getId().toString()))
            .body(carRideDTO);
    }

    /**
     * {@code PUT  /car-rides/:id} : Updates an existing carRide.
     *
     * @param id the id of the carRideDTO to save.
     * @param carRideDTO the carRideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carRideDTO,
     * or with status {@code 400 (Bad Request)} if the carRideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carRideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarRideDTO> updateCarRide(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarRideDTO carRideDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CarRide : {}, {}", id, carRideDTO);
        if (carRideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carRideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carRideDTO = carRideService.update(carRideDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carRideDTO.getId().toString()))
            .body(carRideDTO);
    }

    /**
     * {@code PATCH  /car-rides/:id} : Partial updates given fields of an existing carRide, field will ignore if it is null
     *
     * @param id the id of the carRideDTO to save.
     * @param carRideDTO the carRideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carRideDTO,
     * or with status {@code 400 (Bad Request)} if the carRideDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carRideDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carRideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarRideDTO> partialUpdateCarRide(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarRideDTO carRideDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarRide partially : {}, {}", id, carRideDTO);
        if (carRideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carRideDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carRideRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarRideDTO> result = carRideService.partialUpdate(carRideDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carRideDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /car-rides} : get all the carRides.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carRides in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarRideDTO>> getAllCarRides(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CarRides");
        Page<CarRideDTO> page = carRideService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-rides/:id} : get the "id" carRide.
     *
     * @param id the id of the carRideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carRideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarRideDTO> getCarRide(@PathVariable("id") Long id) {
        log.debug("REST request to get CarRide : {}", id);
        Optional<CarRideDTO> carRideDTO = carRideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carRideDTO);
    }

    /**
     * {@code DELETE  /car-rides/:id} : delete the "id" carRide.
     *
     * @param id the id of the carRideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarRide(@PathVariable("id") Long id) {
        log.debug("REST request to delete CarRide : {}", id);
        carRideService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
