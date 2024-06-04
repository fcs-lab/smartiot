package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleSubStatusRepository;
import br.com.supera.smartiot.service.VehicleSubStatusService;
import br.com.supera.smartiot.service.dto.VehicleSubStatusDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleSubStatus}.
 */
@RestController
@RequestMapping("/api/vehicle-sub-statuses")
public class VehicleSubStatusResource {

    private final Logger log = LoggerFactory.getLogger(VehicleSubStatusResource.class);

    private static final String ENTITY_NAME = "vehicleSubStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleSubStatusService vehicleSubStatusService;

    private final VehicleSubStatusRepository vehicleSubStatusRepository;

    public VehicleSubStatusResource(
        VehicleSubStatusService vehicleSubStatusService,
        VehicleSubStatusRepository vehicleSubStatusRepository
    ) {
        this.vehicleSubStatusService = vehicleSubStatusService;
        this.vehicleSubStatusRepository = vehicleSubStatusRepository;
    }

    /**
     * {@code POST  /vehicle-sub-statuses} : Create a new vehicleSubStatus.
     *
     * @param vehicleSubStatusDTO the vehicleSubStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleSubStatusDTO, or with status {@code 400 (Bad Request)} if the vehicleSubStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleSubStatusDTO> createVehicleSubStatus(@Valid @RequestBody VehicleSubStatusDTO vehicleSubStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleSubStatus : {}", vehicleSubStatusDTO);
        if (vehicleSubStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleSubStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleSubStatusDTO = vehicleSubStatusService.save(vehicleSubStatusDTO);
        return ResponseEntity.created(new URI("/api/vehicle-sub-statuses/" + vehicleSubStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleSubStatusDTO.getId().toString()))
            .body(vehicleSubStatusDTO);
    }

    /**
     * {@code PUT  /vehicle-sub-statuses/:id} : Updates an existing vehicleSubStatus.
     *
     * @param id the id of the vehicleSubStatusDTO to save.
     * @param vehicleSubStatusDTO the vehicleSubStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleSubStatusDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleSubStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleSubStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleSubStatusDTO> updateVehicleSubStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleSubStatusDTO vehicleSubStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleSubStatus : {}, {}", id, vehicleSubStatusDTO);
        if (vehicleSubStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleSubStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleSubStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleSubStatusDTO = vehicleSubStatusService.update(vehicleSubStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleSubStatusDTO.getId().toString()))
            .body(vehicleSubStatusDTO);
    }

    /**
     * {@code PATCH  /vehicle-sub-statuses/:id} : Partial updates given fields of an existing vehicleSubStatus, field will ignore if it is null
     *
     * @param id the id of the vehicleSubStatusDTO to save.
     * @param vehicleSubStatusDTO the vehicleSubStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleSubStatusDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleSubStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleSubStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleSubStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleSubStatusDTO> partialUpdateVehicleSubStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleSubStatusDTO vehicleSubStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleSubStatus partially : {}, {}", id, vehicleSubStatusDTO);
        if (vehicleSubStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleSubStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleSubStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleSubStatusDTO> result = vehicleSubStatusService.partialUpdate(vehicleSubStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleSubStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-sub-statuses} : get all the vehicleSubStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleSubStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleSubStatusDTO>> getAllVehicleSubStatuses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VehicleSubStatuses");
        Page<VehicleSubStatusDTO> page = vehicleSubStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-sub-statuses/:id} : get the "id" vehicleSubStatus.
     *
     * @param id the id of the vehicleSubStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleSubStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleSubStatusDTO> getVehicleSubStatus(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleSubStatus : {}", id);
        Optional<VehicleSubStatusDTO> vehicleSubStatusDTO = vehicleSubStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleSubStatusDTO);
    }

    /**
     * {@code DELETE  /vehicle-sub-statuses/:id} : delete the "id" vehicleSubStatus.
     *
     * @param id the id of the vehicleSubStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleSubStatus(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleSubStatus : {}", id);
        vehicleSubStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
