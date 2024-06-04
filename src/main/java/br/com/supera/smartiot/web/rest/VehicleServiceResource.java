package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleServiceRepository;
import br.com.supera.smartiot.service.VehicleServiceService;
import br.com.supera.smartiot.service.dto.VehicleServiceDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleService}.
 */
@RestController
@RequestMapping("/api/vehicle-services")
public class VehicleServiceResource {

    private final Logger log = LoggerFactory.getLogger(VehicleServiceResource.class);

    private static final String ENTITY_NAME = "vehicleService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleServiceService vehicleServiceService;

    private final VehicleServiceRepository vehicleServiceRepository;

    public VehicleServiceResource(VehicleServiceService vehicleServiceService, VehicleServiceRepository vehicleServiceRepository) {
        this.vehicleServiceService = vehicleServiceService;
        this.vehicleServiceRepository = vehicleServiceRepository;
    }

    /**
     * {@code POST  /vehicle-services} : Create a new vehicleService.
     *
     * @param vehicleServiceDTO the vehicleServiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleServiceDTO, or with status {@code 400 (Bad Request)} if the vehicleService has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleServiceDTO> createVehicleService(@Valid @RequestBody VehicleServiceDTO vehicleServiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleService : {}", vehicleServiceDTO);
        if (vehicleServiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleServiceDTO = vehicleServiceService.save(vehicleServiceDTO);
        return ResponseEntity.created(new URI("/api/vehicle-services/" + vehicleServiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleServiceDTO.getId().toString()))
            .body(vehicleServiceDTO);
    }

    /**
     * {@code PUT  /vehicle-services/:id} : Updates an existing vehicleService.
     *
     * @param id the id of the vehicleServiceDTO to save.
     * @param vehicleServiceDTO the vehicleServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleServiceDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleServiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleServiceDTO> updateVehicleService(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleServiceDTO vehicleServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleService : {}, {}", id, vehicleServiceDTO);
        if (vehicleServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleServiceDTO = vehicleServiceService.update(vehicleServiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleServiceDTO.getId().toString()))
            .body(vehicleServiceDTO);
    }

    /**
     * {@code PATCH  /vehicle-services/:id} : Partial updates given fields of an existing vehicleService, field will ignore if it is null
     *
     * @param id the id of the vehicleServiceDTO to save.
     * @param vehicleServiceDTO the vehicleServiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleServiceDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleServiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleServiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleServiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleServiceDTO> partialUpdateVehicleService(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleServiceDTO vehicleServiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleService partially : {}, {}", id, vehicleServiceDTO);
        if (vehicleServiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleServiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleServiceDTO> result = vehicleServiceService.partialUpdate(vehicleServiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleServiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-services} : get all the vehicleServices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleServices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleServiceDTO>> getAllVehicleServices(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VehicleServices");
        Page<VehicleServiceDTO> page = vehicleServiceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-services/:id} : get the "id" vehicleService.
     *
     * @param id the id of the vehicleServiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleServiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleServiceDTO> getVehicleService(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleService : {}", id);
        Optional<VehicleServiceDTO> vehicleServiceDTO = vehicleServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleServiceDTO);
    }

    /**
     * {@code DELETE  /vehicle-services/:id} : delete the "id" vehicleService.
     *
     * @param id the id of the vehicleServiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleService(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleService : {}", id);
        vehicleServiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
