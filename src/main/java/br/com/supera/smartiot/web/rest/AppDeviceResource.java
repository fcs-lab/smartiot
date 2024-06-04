package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.AppDeviceRepository;
import br.com.supera.smartiot.service.AppDeviceService;
import br.com.supera.smartiot.service.dto.AppDeviceDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.AppDevice}.
 */
@RestController
@RequestMapping("/api/app-devices")
public class AppDeviceResource {

    private final Logger log = LoggerFactory.getLogger(AppDeviceResource.class);

    private static final String ENTITY_NAME = "appDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppDeviceService appDeviceService;

    private final AppDeviceRepository appDeviceRepository;

    public AppDeviceResource(AppDeviceService appDeviceService, AppDeviceRepository appDeviceRepository) {
        this.appDeviceService = appDeviceService;
        this.appDeviceRepository = appDeviceRepository;
    }

    /**
     * {@code POST  /app-devices} : Create a new appDevice.
     *
     * @param appDeviceDTO the appDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appDeviceDTO, or with status {@code 400 (Bad Request)} if the appDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppDeviceDTO> createAppDevice(@Valid @RequestBody AppDeviceDTO appDeviceDTO) throws URISyntaxException {
        log.debug("REST request to save AppDevice : {}", appDeviceDTO);
        if (appDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new appDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appDeviceDTO = appDeviceService.save(appDeviceDTO);
        return ResponseEntity.created(new URI("/api/app-devices/" + appDeviceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appDeviceDTO.getId().toString()))
            .body(appDeviceDTO);
    }

    /**
     * {@code PUT  /app-devices/:id} : Updates an existing appDevice.
     *
     * @param id the id of the appDeviceDTO to save.
     * @param appDeviceDTO the appDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the appDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppDeviceDTO> updateAppDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppDeviceDTO appDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppDevice : {}, {}", id, appDeviceDTO);
        if (appDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appDeviceDTO = appDeviceService.update(appDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDeviceDTO.getId().toString()))
            .body(appDeviceDTO);
    }

    /**
     * {@code PATCH  /app-devices/:id} : Partial updates given fields of an existing appDevice, field will ignore if it is null
     *
     * @param id the id of the appDeviceDTO to save.
     * @param appDeviceDTO the appDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the appDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppDeviceDTO> partialUpdateAppDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppDeviceDTO appDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppDevice partially : {}, {}", id, appDeviceDTO);
        if (appDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppDeviceDTO> result = appDeviceService.partialUpdate(appDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-devices} : get all the appDevices.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appDevices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppDeviceDTO>> getAllAppDevices(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of AppDevices");
        Page<AppDeviceDTO> page = appDeviceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-devices/:id} : get the "id" appDevice.
     *
     * @param id the id of the appDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppDeviceDTO> getAppDevice(@PathVariable("id") Long id) {
        log.debug("REST request to get AppDevice : {}", id);
        Optional<AppDeviceDTO> appDeviceDTO = appDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appDeviceDTO);
    }

    /**
     * {@code DELETE  /app-devices/:id} : delete the "id" appDevice.
     *
     * @param id the id of the appDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppDevice(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppDevice : {}", id);
        appDeviceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
