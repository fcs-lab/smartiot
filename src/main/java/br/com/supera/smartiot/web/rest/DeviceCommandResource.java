package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.DeviceCommandRepository;
import br.com.supera.smartiot.service.DeviceCommandService;
import br.com.supera.smartiot.service.dto.DeviceCommandDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.DeviceCommand}.
 */
@RestController
@RequestMapping("/api/device-commands")
public class DeviceCommandResource {

    private final Logger log = LoggerFactory.getLogger(DeviceCommandResource.class);

    private static final String ENTITY_NAME = "deviceCommand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceCommandService deviceCommandService;

    private final DeviceCommandRepository deviceCommandRepository;

    public DeviceCommandResource(DeviceCommandService deviceCommandService, DeviceCommandRepository deviceCommandRepository) {
        this.deviceCommandService = deviceCommandService;
        this.deviceCommandRepository = deviceCommandRepository;
    }

    /**
     * {@code POST  /device-commands} : Create a new deviceCommand.
     *
     * @param deviceCommandDTO the deviceCommandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceCommandDTO, or with status {@code 400 (Bad Request)} if the deviceCommand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeviceCommandDTO> createDeviceCommand(@Valid @RequestBody DeviceCommandDTO deviceCommandDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeviceCommand : {}", deviceCommandDTO);
        if (deviceCommandDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceCommand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deviceCommandDTO = deviceCommandService.save(deviceCommandDTO);
        return ResponseEntity.created(new URI("/api/device-commands/" + deviceCommandDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deviceCommandDTO.getId().toString()))
            .body(deviceCommandDTO);
    }

    /**
     * {@code PUT  /device-commands/:id} : Updates an existing deviceCommand.
     *
     * @param id the id of the deviceCommandDTO to save.
     * @param deviceCommandDTO the deviceCommandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceCommandDTO,
     * or with status {@code 400 (Bad Request)} if the deviceCommandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceCommandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeviceCommandDTO> updateDeviceCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeviceCommandDTO deviceCommandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeviceCommand : {}, {}", id, deviceCommandDTO);
        if (deviceCommandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceCommandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceCommandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deviceCommandDTO = deviceCommandService.update(deviceCommandDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceCommandDTO.getId().toString()))
            .body(deviceCommandDTO);
    }

    /**
     * {@code PATCH  /device-commands/:id} : Partial updates given fields of an existing deviceCommand, field will ignore if it is null
     *
     * @param id the id of the deviceCommandDTO to save.
     * @param deviceCommandDTO the deviceCommandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceCommandDTO,
     * or with status {@code 400 (Bad Request)} if the deviceCommandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceCommandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceCommandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceCommandDTO> partialUpdateDeviceCommand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeviceCommandDTO deviceCommandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeviceCommand partially : {}, {}", id, deviceCommandDTO);
        if (deviceCommandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceCommandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceCommandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceCommandDTO> result = deviceCommandService.partialUpdate(deviceCommandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceCommandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-commands} : get all the deviceCommands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceCommands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DeviceCommandDTO>> getAllDeviceCommands(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of DeviceCommands");
        Page<DeviceCommandDTO> page = deviceCommandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-commands/:id} : get the "id" deviceCommand.
     *
     * @param id the id of the deviceCommandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceCommandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceCommandDTO> getDeviceCommand(@PathVariable("id") Long id) {
        log.debug("REST request to get DeviceCommand : {}", id);
        Optional<DeviceCommandDTO> deviceCommandDTO = deviceCommandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceCommandDTO);
    }

    /**
     * {@code DELETE  /device-commands/:id} : delete the "id" deviceCommand.
     *
     * @param id the id of the deviceCommandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeviceCommand(@PathVariable("id") Long id) {
        log.debug("REST request to delete DeviceCommand : {}", id);
        deviceCommandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
