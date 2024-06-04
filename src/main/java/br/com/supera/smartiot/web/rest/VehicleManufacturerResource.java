package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleManufacturerRepository;
import br.com.supera.smartiot.service.VehicleManufacturerService;
import br.com.supera.smartiot.service.dto.VehicleManufacturerDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleManufacturer}.
 */
@RestController
@RequestMapping("/api/vehicle-manufacturers")
public class VehicleManufacturerResource {

    private final Logger log = LoggerFactory.getLogger(VehicleManufacturerResource.class);

    private static final String ENTITY_NAME = "vehicleManufacturer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleManufacturerService vehicleManufacturerService;

    private final VehicleManufacturerRepository vehicleManufacturerRepository;

    public VehicleManufacturerResource(
        VehicleManufacturerService vehicleManufacturerService,
        VehicleManufacturerRepository vehicleManufacturerRepository
    ) {
        this.vehicleManufacturerService = vehicleManufacturerService;
        this.vehicleManufacturerRepository = vehicleManufacturerRepository;
    }

    /**
     * {@code POST  /vehicle-manufacturers} : Create a new vehicleManufacturer.
     *
     * @param vehicleManufacturerDTO the vehicleManufacturerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleManufacturerDTO, or with status {@code 400 (Bad Request)} if the vehicleManufacturer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleManufacturerDTO> createVehicleManufacturer(
        @Valid @RequestBody VehicleManufacturerDTO vehicleManufacturerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save VehicleManufacturer : {}", vehicleManufacturerDTO);
        if (vehicleManufacturerDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleManufacturer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleManufacturerDTO = vehicleManufacturerService.save(vehicleManufacturerDTO);
        return ResponseEntity.created(new URI("/api/vehicle-manufacturers/" + vehicleManufacturerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleManufacturerDTO.getId().toString()))
            .body(vehicleManufacturerDTO);
    }

    /**
     * {@code PUT  /vehicle-manufacturers/:id} : Updates an existing vehicleManufacturer.
     *
     * @param id the id of the vehicleManufacturerDTO to save.
     * @param vehicleManufacturerDTO the vehicleManufacturerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleManufacturerDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleManufacturerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleManufacturerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleManufacturerDTO> updateVehicleManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleManufacturerDTO vehicleManufacturerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleManufacturer : {}, {}", id, vehicleManufacturerDTO);
        if (vehicleManufacturerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleManufacturerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleManufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleManufacturerDTO = vehicleManufacturerService.update(vehicleManufacturerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleManufacturerDTO.getId().toString()))
            .body(vehicleManufacturerDTO);
    }

    /**
     * {@code PATCH  /vehicle-manufacturers/:id} : Partial updates given fields of an existing vehicleManufacturer, field will ignore if it is null
     *
     * @param id the id of the vehicleManufacturerDTO to save.
     * @param vehicleManufacturerDTO the vehicleManufacturerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleManufacturerDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleManufacturerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleManufacturerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleManufacturerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleManufacturerDTO> partialUpdateVehicleManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleManufacturerDTO vehicleManufacturerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleManufacturer partially : {}, {}", id, vehicleManufacturerDTO);
        if (vehicleManufacturerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleManufacturerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleManufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleManufacturerDTO> result = vehicleManufacturerService.partialUpdate(vehicleManufacturerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleManufacturerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-manufacturers} : get all the vehicleManufacturers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleManufacturers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleManufacturerDTO>> getAllVehicleManufacturers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VehicleManufacturers");
        Page<VehicleManufacturerDTO> page = vehicleManufacturerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-manufacturers/:id} : get the "id" vehicleManufacturer.
     *
     * @param id the id of the vehicleManufacturerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleManufacturerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleManufacturerDTO> getVehicleManufacturer(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleManufacturer : {}", id);
        Optional<VehicleManufacturerDTO> vehicleManufacturerDTO = vehicleManufacturerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleManufacturerDTO);
    }

    /**
     * {@code DELETE  /vehicle-manufacturers/:id} : delete the "id" vehicleManufacturer.
     *
     * @param id the id of the vehicleManufacturerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleManufacturer(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleManufacturer : {}", id);
        vehicleManufacturerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
