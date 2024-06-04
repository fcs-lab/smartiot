package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.WaterAlertRepository;
import br.com.supera.smartiot.service.WaterAlertService;
import br.com.supera.smartiot.service.dto.WaterAlertDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.WaterAlert}.
 */
@RestController
@RequestMapping("/api/water-alerts")
public class WaterAlertResource {

    private final Logger log = LoggerFactory.getLogger(WaterAlertResource.class);

    private static final String ENTITY_NAME = "waterAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaterAlertService waterAlertService;

    private final WaterAlertRepository waterAlertRepository;

    public WaterAlertResource(WaterAlertService waterAlertService, WaterAlertRepository waterAlertRepository) {
        this.waterAlertService = waterAlertService;
        this.waterAlertRepository = waterAlertRepository;
    }

    /**
     * {@code POST  /water-alerts} : Create a new waterAlert.
     *
     * @param waterAlertDTO the waterAlertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waterAlertDTO, or with status {@code 400 (Bad Request)} if the waterAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WaterAlertDTO> createWaterAlert(@Valid @RequestBody WaterAlertDTO waterAlertDTO) throws URISyntaxException {
        log.debug("REST request to save WaterAlert : {}", waterAlertDTO);
        if (waterAlertDTO.getId() != null) {
            throw new BadRequestAlertException("A new waterAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        waterAlertDTO = waterAlertService.save(waterAlertDTO);
        return ResponseEntity.created(new URI("/api/water-alerts/" + waterAlertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, waterAlertDTO.getId().toString()))
            .body(waterAlertDTO);
    }

    /**
     * {@code PUT  /water-alerts/:id} : Updates an existing waterAlert.
     *
     * @param id the id of the waterAlertDTO to save.
     * @param waterAlertDTO the waterAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterAlertDTO,
     * or with status {@code 400 (Bad Request)} if the waterAlertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waterAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WaterAlertDTO> updateWaterAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaterAlertDTO waterAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaterAlert : {}, {}", id, waterAlertDTO);
        if (waterAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        waterAlertDTO = waterAlertService.update(waterAlertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterAlertDTO.getId().toString()))
            .body(waterAlertDTO);
    }

    /**
     * {@code PATCH  /water-alerts/:id} : Partial updates given fields of an existing waterAlert, field will ignore if it is null
     *
     * @param id the id of the waterAlertDTO to save.
     * @param waterAlertDTO the waterAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterAlertDTO,
     * or with status {@code 400 (Bad Request)} if the waterAlertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waterAlertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waterAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaterAlertDTO> partialUpdateWaterAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaterAlertDTO waterAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaterAlert partially : {}, {}", id, waterAlertDTO);
        if (waterAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaterAlertDTO> result = waterAlertService.partialUpdate(waterAlertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterAlertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /water-alerts} : get all the waterAlerts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waterAlerts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WaterAlertDTO>> getAllWaterAlerts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WaterAlerts");
        Page<WaterAlertDTO> page = waterAlertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /water-alerts/:id} : get the "id" waterAlert.
     *
     * @param id the id of the waterAlertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waterAlertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WaterAlertDTO> getWaterAlert(@PathVariable("id") Long id) {
        log.debug("REST request to get WaterAlert : {}", id);
        Optional<WaterAlertDTO> waterAlertDTO = waterAlertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waterAlertDTO);
    }

    /**
     * {@code DELETE  /water-alerts/:id} : delete the "id" waterAlert.
     *
     * @param id the id of the waterAlertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterAlert(@PathVariable("id") Long id) {
        log.debug("REST request to delete WaterAlert : {}", id);
        waterAlertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
