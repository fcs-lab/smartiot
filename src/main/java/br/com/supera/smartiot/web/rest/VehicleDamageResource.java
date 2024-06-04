package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleDamageRepository;
import br.com.supera.smartiot.service.VehicleDamageService;
import br.com.supera.smartiot.service.dto.VehicleDamageDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleDamage}.
 */
@RestController
@RequestMapping("/api/vehicle-damages")
public class VehicleDamageResource {

    private final Logger log = LoggerFactory.getLogger(VehicleDamageResource.class);

    private static final String ENTITY_NAME = "vehicleDamage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleDamageService vehicleDamageService;

    private final VehicleDamageRepository vehicleDamageRepository;

    public VehicleDamageResource(VehicleDamageService vehicleDamageService, VehicleDamageRepository vehicleDamageRepository) {
        this.vehicleDamageService = vehicleDamageService;
        this.vehicleDamageRepository = vehicleDamageRepository;
    }

    /**
     * {@code POST  /vehicle-damages} : Create a new vehicleDamage.
     *
     * @param vehicleDamageDTO the vehicleDamageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleDamageDTO, or with status {@code 400 (Bad Request)} if the vehicleDamage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleDamageDTO> createVehicleDamage(@Valid @RequestBody VehicleDamageDTO vehicleDamageDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleDamage : {}", vehicleDamageDTO);
        if (vehicleDamageDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleDamage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleDamageDTO = vehicleDamageService.save(vehicleDamageDTO);
        return ResponseEntity.created(new URI("/api/vehicle-damages/" + vehicleDamageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleDamageDTO.getId().toString()))
            .body(vehicleDamageDTO);
    }

    /**
     * {@code PUT  /vehicle-damages/:id} : Updates an existing vehicleDamage.
     *
     * @param id the id of the vehicleDamageDTO to save.
     * @param vehicleDamageDTO the vehicleDamageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleDamageDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleDamageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleDamageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleDamageDTO> updateVehicleDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleDamageDTO vehicleDamageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleDamage : {}, {}", id, vehicleDamageDTO);
        if (vehicleDamageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleDamageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleDamageDTO = vehicleDamageService.update(vehicleDamageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleDamageDTO.getId().toString()))
            .body(vehicleDamageDTO);
    }

    /**
     * {@code PATCH  /vehicle-damages/:id} : Partial updates given fields of an existing vehicleDamage, field will ignore if it is null
     *
     * @param id the id of the vehicleDamageDTO to save.
     * @param vehicleDamageDTO the vehicleDamageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleDamageDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleDamageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleDamageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleDamageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleDamageDTO> partialUpdateVehicleDamage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleDamageDTO vehicleDamageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleDamage partially : {}, {}", id, vehicleDamageDTO);
        if (vehicleDamageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleDamageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleDamageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleDamageDTO> result = vehicleDamageService.partialUpdate(vehicleDamageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleDamageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-damages} : get all the vehicleDamages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleDamages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleDamageDTO>> getAllVehicleDamages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VehicleDamages");
        Page<VehicleDamageDTO> page = vehicleDamageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-damages/:id} : get the "id" vehicleDamage.
     *
     * @param id the id of the vehicleDamageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleDamageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleDamageDTO> getVehicleDamage(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleDamage : {}", id);
        Optional<VehicleDamageDTO> vehicleDamageDTO = vehicleDamageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleDamageDTO);
    }

    /**
     * {@code DELETE  /vehicle-damages/:id} : delete the "id" vehicleDamage.
     *
     * @param id the id of the vehicleDamageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleDamage(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleDamage : {}", id);
        vehicleDamageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
