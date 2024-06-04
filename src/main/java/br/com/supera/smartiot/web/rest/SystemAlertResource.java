package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.SystemAlertRepository;
import br.com.supera.smartiot.service.SystemAlertService;
import br.com.supera.smartiot.service.dto.SystemAlertDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.SystemAlert}.
 */
@RestController
@RequestMapping("/api/system-alerts")
public class SystemAlertResource {

    private final Logger log = LoggerFactory.getLogger(SystemAlertResource.class);

    private static final String ENTITY_NAME = "systemAlert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemAlertService systemAlertService;

    private final SystemAlertRepository systemAlertRepository;

    public SystemAlertResource(SystemAlertService systemAlertService, SystemAlertRepository systemAlertRepository) {
        this.systemAlertService = systemAlertService;
        this.systemAlertRepository = systemAlertRepository;
    }

    /**
     * {@code POST  /system-alerts} : Create a new systemAlert.
     *
     * @param systemAlertDTO the systemAlertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemAlertDTO, or with status {@code 400 (Bad Request)} if the systemAlert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemAlertDTO> createSystemAlert(@Valid @RequestBody SystemAlertDTO systemAlertDTO) throws URISyntaxException {
        log.debug("REST request to save SystemAlert : {}", systemAlertDTO);
        if (systemAlertDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemAlert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemAlertDTO = systemAlertService.save(systemAlertDTO);
        return ResponseEntity.created(new URI("/api/system-alerts/" + systemAlertDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemAlertDTO.getId().toString()))
            .body(systemAlertDTO);
    }

    /**
     * {@code PUT  /system-alerts/:id} : Updates an existing systemAlert.
     *
     * @param id the id of the systemAlertDTO to save.
     * @param systemAlertDTO the systemAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemAlertDTO,
     * or with status {@code 400 (Bad Request)} if the systemAlertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemAlertDTO> updateSystemAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemAlertDTO systemAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemAlert : {}, {}", id, systemAlertDTO);
        if (systemAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemAlertDTO = systemAlertService.update(systemAlertDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemAlertDTO.getId().toString()))
            .body(systemAlertDTO);
    }

    /**
     * {@code PATCH  /system-alerts/:id} : Partial updates given fields of an existing systemAlert, field will ignore if it is null
     *
     * @param id the id of the systemAlertDTO to save.
     * @param systemAlertDTO the systemAlertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemAlertDTO,
     * or with status {@code 400 (Bad Request)} if the systemAlertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemAlertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemAlertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemAlertDTO> partialUpdateSystemAlert(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemAlertDTO systemAlertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemAlert partially : {}, {}", id, systemAlertDTO);
        if (systemAlertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemAlertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemAlertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemAlertDTO> result = systemAlertService.partialUpdate(systemAlertDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemAlertDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-alerts} : get all the systemAlerts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemAlerts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemAlertDTO>> getAllSystemAlerts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SystemAlerts");
        Page<SystemAlertDTO> page = systemAlertService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-alerts/:id} : get the "id" systemAlert.
     *
     * @param id the id of the systemAlertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemAlertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemAlertDTO> getSystemAlert(@PathVariable("id") Long id) {
        log.debug("REST request to get SystemAlert : {}", id);
        Optional<SystemAlertDTO> systemAlertDTO = systemAlertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemAlertDTO);
    }

    /**
     * {@code DELETE  /system-alerts/:id} : delete the "id" systemAlert.
     *
     * @param id the id of the systemAlertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemAlert(@PathVariable("id") Long id) {
        log.debug("REST request to delete SystemAlert : {}", id);
        systemAlertService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
