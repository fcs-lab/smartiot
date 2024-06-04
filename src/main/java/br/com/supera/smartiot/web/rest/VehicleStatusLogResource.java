package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleStatusLogRepository;
import br.com.supera.smartiot.service.VehicleStatusLogService;
import br.com.supera.smartiot.service.dto.VehicleStatusLogDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleStatusLog}.
 */
@RestController
@RequestMapping("/api/vehicle-status-logs")
public class VehicleStatusLogResource {

    private final Logger log = LoggerFactory.getLogger(VehicleStatusLogResource.class);

    private static final String ENTITY_NAME = "vehicleStatusLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleStatusLogService vehicleStatusLogService;

    private final VehicleStatusLogRepository vehicleStatusLogRepository;

    public VehicleStatusLogResource(
        VehicleStatusLogService vehicleStatusLogService,
        VehicleStatusLogRepository vehicleStatusLogRepository
    ) {
        this.vehicleStatusLogService = vehicleStatusLogService;
        this.vehicleStatusLogRepository = vehicleStatusLogRepository;
    }

    /**
     * {@code POST  /vehicle-status-logs} : Create a new vehicleStatusLog.
     *
     * @param vehicleStatusLogDTO the vehicleStatusLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleStatusLogDTO, or with status {@code 400 (Bad Request)} if the vehicleStatusLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleStatusLogDTO> createVehicleStatusLog(@Valid @RequestBody VehicleStatusLogDTO vehicleStatusLogDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleStatusLog : {}", vehicleStatusLogDTO);
        if (vehicleStatusLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleStatusLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleStatusLogDTO = vehicleStatusLogService.save(vehicleStatusLogDTO);
        return ResponseEntity.created(new URI("/api/vehicle-status-logs/" + vehicleStatusLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleStatusLogDTO.getId().toString()))
            .body(vehicleStatusLogDTO);
    }

    /**
     * {@code PUT  /vehicle-status-logs/:id} : Updates an existing vehicleStatusLog.
     *
     * @param id the id of the vehicleStatusLogDTO to save.
     * @param vehicleStatusLogDTO the vehicleStatusLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleStatusLogDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleStatusLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleStatusLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleStatusLogDTO> updateVehicleStatusLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleStatusLogDTO vehicleStatusLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleStatusLog : {}, {}", id, vehicleStatusLogDTO);
        if (vehicleStatusLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleStatusLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleStatusLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleStatusLogDTO = vehicleStatusLogService.update(vehicleStatusLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleStatusLogDTO.getId().toString()))
            .body(vehicleStatusLogDTO);
    }

    /**
     * {@code PATCH  /vehicle-status-logs/:id} : Partial updates given fields of an existing vehicleStatusLog, field will ignore if it is null
     *
     * @param id the id of the vehicleStatusLogDTO to save.
     * @param vehicleStatusLogDTO the vehicleStatusLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleStatusLogDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleStatusLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleStatusLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleStatusLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleStatusLogDTO> partialUpdateVehicleStatusLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleStatusLogDTO vehicleStatusLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleStatusLog partially : {}, {}", id, vehicleStatusLogDTO);
        if (vehicleStatusLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleStatusLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleStatusLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleStatusLogDTO> result = vehicleStatusLogService.partialUpdate(vehicleStatusLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleStatusLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-status-logs} : get all the vehicleStatusLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleStatusLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleStatusLogDTO>> getAllVehicleStatusLogs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of VehicleStatusLogs");
        Page<VehicleStatusLogDTO> page = vehicleStatusLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-status-logs/:id} : get the "id" vehicleStatusLog.
     *
     * @param id the id of the vehicleStatusLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleStatusLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleStatusLogDTO> getVehicleStatusLog(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleStatusLog : {}", id);
        Optional<VehicleStatusLogDTO> vehicleStatusLogDTO = vehicleStatusLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleStatusLogDTO);
    }

    /**
     * {@code DELETE  /vehicle-status-logs/:id} : delete the "id" vehicleStatusLog.
     *
     * @param id the id of the vehicleStatusLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleStatusLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleStatusLog : {}", id);
        vehicleStatusLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
