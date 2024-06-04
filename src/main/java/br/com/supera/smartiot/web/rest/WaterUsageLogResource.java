package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.WaterUsageLogRepository;
import br.com.supera.smartiot.service.WaterUsageLogService;
import br.com.supera.smartiot.service.dto.WaterUsageLogDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.WaterUsageLog}.
 */
@RestController
@RequestMapping("/api/water-usage-logs")
public class WaterUsageLogResource {

    private final Logger log = LoggerFactory.getLogger(WaterUsageLogResource.class);

    private static final String ENTITY_NAME = "waterUsageLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaterUsageLogService waterUsageLogService;

    private final WaterUsageLogRepository waterUsageLogRepository;

    public WaterUsageLogResource(WaterUsageLogService waterUsageLogService, WaterUsageLogRepository waterUsageLogRepository) {
        this.waterUsageLogService = waterUsageLogService;
        this.waterUsageLogRepository = waterUsageLogRepository;
    }

    /**
     * {@code POST  /water-usage-logs} : Create a new waterUsageLog.
     *
     * @param waterUsageLogDTO the waterUsageLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waterUsageLogDTO, or with status {@code 400 (Bad Request)} if the waterUsageLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WaterUsageLogDTO> createWaterUsageLog(@Valid @RequestBody WaterUsageLogDTO waterUsageLogDTO)
        throws URISyntaxException {
        log.debug("REST request to save WaterUsageLog : {}", waterUsageLogDTO);
        if (waterUsageLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new waterUsageLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        waterUsageLogDTO = waterUsageLogService.save(waterUsageLogDTO);
        return ResponseEntity.created(new URI("/api/water-usage-logs/" + waterUsageLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, waterUsageLogDTO.getId().toString()))
            .body(waterUsageLogDTO);
    }

    /**
     * {@code PUT  /water-usage-logs/:id} : Updates an existing waterUsageLog.
     *
     * @param id the id of the waterUsageLogDTO to save.
     * @param waterUsageLogDTO the waterUsageLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterUsageLogDTO,
     * or with status {@code 400 (Bad Request)} if the waterUsageLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waterUsageLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WaterUsageLogDTO> updateWaterUsageLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaterUsageLogDTO waterUsageLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaterUsageLog : {}, {}", id, waterUsageLogDTO);
        if (waterUsageLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterUsageLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterUsageLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        waterUsageLogDTO = waterUsageLogService.update(waterUsageLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterUsageLogDTO.getId().toString()))
            .body(waterUsageLogDTO);
    }

    /**
     * {@code PATCH  /water-usage-logs/:id} : Partial updates given fields of an existing waterUsageLog, field will ignore if it is null
     *
     * @param id the id of the waterUsageLogDTO to save.
     * @param waterUsageLogDTO the waterUsageLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterUsageLogDTO,
     * or with status {@code 400 (Bad Request)} if the waterUsageLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waterUsageLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waterUsageLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaterUsageLogDTO> partialUpdateWaterUsageLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaterUsageLogDTO waterUsageLogDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaterUsageLog partially : {}, {}", id, waterUsageLogDTO);
        if (waterUsageLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterUsageLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterUsageLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaterUsageLogDTO> result = waterUsageLogService.partialUpdate(waterUsageLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterUsageLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /water-usage-logs} : get all the waterUsageLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waterUsageLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WaterUsageLogDTO>> getAllWaterUsageLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WaterUsageLogs");
        Page<WaterUsageLogDTO> page = waterUsageLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /water-usage-logs/:id} : get the "id" waterUsageLog.
     *
     * @param id the id of the waterUsageLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waterUsageLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WaterUsageLogDTO> getWaterUsageLog(@PathVariable("id") Long id) {
        log.debug("REST request to get WaterUsageLog : {}", id);
        Optional<WaterUsageLogDTO> waterUsageLogDTO = waterUsageLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waterUsageLogDTO);
    }

    /**
     * {@code DELETE  /water-usage-logs/:id} : delete the "id" waterUsageLog.
     *
     * @param id the id of the waterUsageLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterUsageLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete WaterUsageLog : {}", id);
        waterUsageLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
