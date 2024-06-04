package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.WaterSensorRepository;
import br.com.supera.smartiot.service.WaterSensorService;
import br.com.supera.smartiot.service.dto.WaterSensorDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.WaterSensor}.
 */
@RestController
@RequestMapping("/api/water-sensors")
public class WaterSensorResource {

    private final Logger log = LoggerFactory.getLogger(WaterSensorResource.class);

    private static final String ENTITY_NAME = "waterSensor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WaterSensorService waterSensorService;

    private final WaterSensorRepository waterSensorRepository;

    public WaterSensorResource(WaterSensorService waterSensorService, WaterSensorRepository waterSensorRepository) {
        this.waterSensorService = waterSensorService;
        this.waterSensorRepository = waterSensorRepository;
    }

    /**
     * {@code POST  /water-sensors} : Create a new waterSensor.
     *
     * @param waterSensorDTO the waterSensorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new waterSensorDTO, or with status {@code 400 (Bad Request)} if the waterSensor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WaterSensorDTO> createWaterSensor(@Valid @RequestBody WaterSensorDTO waterSensorDTO) throws URISyntaxException {
        log.debug("REST request to save WaterSensor : {}", waterSensorDTO);
        if (waterSensorDTO.getId() != null) {
            throw new BadRequestAlertException("A new waterSensor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        waterSensorDTO = waterSensorService.save(waterSensorDTO);
        return ResponseEntity.created(new URI("/api/water-sensors/" + waterSensorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, waterSensorDTO.getId().toString()))
            .body(waterSensorDTO);
    }

    /**
     * {@code PUT  /water-sensors/:id} : Updates an existing waterSensor.
     *
     * @param id the id of the waterSensorDTO to save.
     * @param waterSensorDTO the waterSensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterSensorDTO,
     * or with status {@code 400 (Bad Request)} if the waterSensorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the waterSensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WaterSensorDTO> updateWaterSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WaterSensorDTO waterSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WaterSensor : {}, {}", id, waterSensorDTO);
        if (waterSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterSensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        waterSensorDTO = waterSensorService.update(waterSensorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterSensorDTO.getId().toString()))
            .body(waterSensorDTO);
    }

    /**
     * {@code PATCH  /water-sensors/:id} : Partial updates given fields of an existing waterSensor, field will ignore if it is null
     *
     * @param id the id of the waterSensorDTO to save.
     * @param waterSensorDTO the waterSensorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated waterSensorDTO,
     * or with status {@code 400 (Bad Request)} if the waterSensorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the waterSensorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the waterSensorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WaterSensorDTO> partialUpdateWaterSensor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WaterSensorDTO waterSensorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WaterSensor partially : {}, {}", id, waterSensorDTO);
        if (waterSensorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, waterSensorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!waterSensorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WaterSensorDTO> result = waterSensorService.partialUpdate(waterSensorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, waterSensorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /water-sensors} : get all the waterSensors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of waterSensors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WaterSensorDTO>> getAllWaterSensors(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WaterSensors");
        Page<WaterSensorDTO> page = waterSensorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /water-sensors/:id} : get the "id" waterSensor.
     *
     * @param id the id of the waterSensorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the waterSensorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WaterSensorDTO> getWaterSensor(@PathVariable("id") Long id) {
        log.debug("REST request to get WaterSensor : {}", id);
        Optional<WaterSensorDTO> waterSensorDTO = waterSensorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(waterSensorDTO);
    }

    /**
     * {@code DELETE  /water-sensors/:id} : delete the "id" waterSensor.
     *
     * @param id the id of the waterSensorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaterSensor(@PathVariable("id") Long id) {
        log.debug("REST request to delete WaterSensor : {}", id);
        waterSensorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
