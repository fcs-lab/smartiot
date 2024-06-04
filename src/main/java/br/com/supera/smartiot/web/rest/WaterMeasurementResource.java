package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.WaterMeasurementRepository;
import br.com.supera.smartiot.service.WaterMeasurementService;
import br.com.supera.smartiot.service.dto.WaterMeasurementDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.WaterMeasurement}.
 */
@RestController
@RequestMapping("/api/water-measurements")
public class WaterMeasurementResource {

    private final Logger log = LoggerFactory.getLogger(WaterMeasurementResource.class);

    private static final String ENTITY_NAME = "waterMeasurement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaterMeasurementService waterMeasurementService;

    private final WaterMeasurementRepository waterMeasurementRepository;

    public WaterMeasurementResource(
        WaterMeasurementService waterMeasurementService,
        WaterMeasurementRepository waterMeasurementRepository
    ) {
        this.waterMeasurementService = waterMeasurementService;
        this.waterMeasurementRepository = waterMeasurementRepository;
    }

    /**
     * {@code POST  /water-measurements} : Create a new waterMeasurement.
     *
     * @param waterMeasurementDTO the waterMeasurementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waterMeasurementDTO, or with status {@code 400 (Bad Request)} if the waterMeasurement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WaterMeasurementDTO> createWaterMeasurement(@Valid @RequestBody WaterMeasurementDTO waterMeasurementDTO)
        throws URISyntaxException {
        log.debug("REST request to save WaterMeasurement : {}", waterMeasurementDTO);
        if (waterMeasurementDTO.getId() != null) {
            throw new BadRequestAlertException("A new waterMeasurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        waterMeasurementDTO = waterMeasurementService.save(waterMeasurementDTO);
        return ResponseEntity.created(new URI("/api/water-measurements/" + waterMeasurementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, waterMeasurementDTO.getId().toString()))
            .body(waterMeasurementDTO);
    }

    /**
     * {@code PUT  /water-measurements/:id} : Updates an existing waterMeasurement.
     *
     * @param id the id of the waterMeasurementDTO to save.
     * @param waterMeasurementDTO the waterMeasurementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterMeasurementDTO,
     * or with status {@code 400 (Bad Request)} if the waterMeasurementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waterMeasurementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WaterMeasurementDTO> updateWaterMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaterMeasurementDTO waterMeasurementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaterMeasurement : {}, {}", id, waterMeasurementDTO);
        if (waterMeasurementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterMeasurementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterMeasurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        waterMeasurementDTO = waterMeasurementService.update(waterMeasurementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterMeasurementDTO.getId().toString()))
            .body(waterMeasurementDTO);
    }

    /**
     * {@code PATCH  /water-measurements/:id} : Partial updates given fields of an existing waterMeasurement, field will ignore if it is null
     *
     * @param id the id of the waterMeasurementDTO to save.
     * @param waterMeasurementDTO the waterMeasurementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterMeasurementDTO,
     * or with status {@code 400 (Bad Request)} if the waterMeasurementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waterMeasurementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waterMeasurementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaterMeasurementDTO> partialUpdateWaterMeasurement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaterMeasurementDTO waterMeasurementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaterMeasurement partially : {}, {}", id, waterMeasurementDTO);
        if (waterMeasurementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterMeasurementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterMeasurementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaterMeasurementDTO> result = waterMeasurementService.partialUpdate(waterMeasurementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterMeasurementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /water-measurements} : get all the waterMeasurements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waterMeasurements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WaterMeasurementDTO>> getAllWaterMeasurements(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of WaterMeasurements");
        Page<WaterMeasurementDTO> page = waterMeasurementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /water-measurements/:id} : get the "id" waterMeasurement.
     *
     * @param id the id of the waterMeasurementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waterMeasurementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WaterMeasurementDTO> getWaterMeasurement(@PathVariable("id") Long id) {
        log.debug("REST request to get WaterMeasurement : {}", id);
        Optional<WaterMeasurementDTO> waterMeasurementDTO = waterMeasurementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waterMeasurementDTO);
    }

    /**
     * {@code DELETE  /water-measurements/:id} : delete the "id" waterMeasurement.
     *
     * @param id the id of the waterMeasurementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterMeasurement(@PathVariable("id") Long id) {
        log.debug("REST request to delete WaterMeasurement : {}", id);
        waterMeasurementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
