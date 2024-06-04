package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.DeviceTelemetryRepository;
import br.com.supera.smartiot.service.DeviceTelemetryService;
import br.com.supera.smartiot.service.dto.DeviceTelemetryDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.DeviceTelemetry}.
 */
@RestController
@RequestMapping("/api/device-telemetries")
public class DeviceTelemetryResource {

    private final Logger log = LoggerFactory.getLogger(DeviceTelemetryResource.class);

    private static final String ENTITY_NAME = "deviceTelemetry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceTelemetryService deviceTelemetryService;

    private final DeviceTelemetryRepository deviceTelemetryRepository;

    public DeviceTelemetryResource(DeviceTelemetryService deviceTelemetryService, DeviceTelemetryRepository deviceTelemetryRepository) {
        this.deviceTelemetryService = deviceTelemetryService;
        this.deviceTelemetryRepository = deviceTelemetryRepository;
    }

    /**
     * {@code POST  /device-telemetries} : Create a new deviceTelemetry.
     *
     * @param deviceTelemetryDTO the deviceTelemetryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceTelemetryDTO, or with status {@code 400 (Bad Request)} if the deviceTelemetry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeviceTelemetryDTO> createDeviceTelemetry(@Valid @RequestBody DeviceTelemetryDTO deviceTelemetryDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeviceTelemetry : {}", deviceTelemetryDTO);
        if (deviceTelemetryDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceTelemetry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deviceTelemetryDTO = deviceTelemetryService.save(deviceTelemetryDTO);
        return ResponseEntity.created(new URI("/api/device-telemetries/" + deviceTelemetryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deviceTelemetryDTO.getId().toString()))
            .body(deviceTelemetryDTO);
    }

    /**
     * {@code PUT  /device-telemetries/:id} : Updates an existing deviceTelemetry.
     *
     * @param id the id of the deviceTelemetryDTO to save.
     * @param deviceTelemetryDTO the deviceTelemetryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceTelemetryDTO,
     * or with status {@code 400 (Bad Request)} if the deviceTelemetryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceTelemetryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeviceTelemetryDTO> updateDeviceTelemetry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeviceTelemetryDTO deviceTelemetryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceTelemetry : {}, {}", id, deviceTelemetryDTO);
        if (deviceTelemetryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceTelemetryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTelemetryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deviceTelemetryDTO = deviceTelemetryService.update(deviceTelemetryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceTelemetryDTO.getId().toString()))
            .body(deviceTelemetryDTO);
    }

    /**
     * {@code PATCH  /device-telemetries/:id} : Partial updates given fields of an existing deviceTelemetry, field will ignore if it is null
     *
     * @param id the id of the deviceTelemetryDTO to save.
     * @param deviceTelemetryDTO the deviceTelemetryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceTelemetryDTO,
     * or with status {@code 400 (Bad Request)} if the deviceTelemetryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceTelemetryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceTelemetryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceTelemetryDTO> partialUpdateDeviceTelemetry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeviceTelemetryDTO deviceTelemetryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceTelemetry partially : {}, {}", id, deviceTelemetryDTO);
        if (deviceTelemetryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceTelemetryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceTelemetryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceTelemetryDTO> result = deviceTelemetryService.partialUpdate(deviceTelemetryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceTelemetryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-telemetries} : get all the deviceTelemetries.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceTelemetries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DeviceTelemetryDTO>> getAllDeviceTelemetries(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DeviceTelemetries");
        Page<DeviceTelemetryDTO> page = deviceTelemetryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-telemetries/:id} : get the "id" deviceTelemetry.
     *
     * @param id the id of the deviceTelemetryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceTelemetryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceTelemetryDTO> getDeviceTelemetry(@PathVariable("id") Long id) {
        log.debug("REST request to get DeviceTelemetry : {}", id);
        Optional<DeviceTelemetryDTO> deviceTelemetryDTO = deviceTelemetryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceTelemetryDTO);
    }

    /**
     * {@code DELETE  /device-telemetries/:id} : delete the "id" deviceTelemetry.
     *
     * @param id the id of the deviceTelemetryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeviceTelemetry(@PathVariable("id") Long id) {
        log.debug("REST request to delete DeviceTelemetry : {}", id);
        deviceTelemetryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
