package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleGroupRepository;
import br.com.supera.smartiot.service.VehicleGroupService;
import br.com.supera.smartiot.service.dto.VehicleGroupDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleGroup}.
 */
@RestController
@RequestMapping("/api/vehicle-groups")
public class VehicleGroupResource {

    private final Logger log = LoggerFactory.getLogger(VehicleGroupResource.class);

    private static final String ENTITY_NAME = "vehicleGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleGroupService vehicleGroupService;

    private final VehicleGroupRepository vehicleGroupRepository;

    public VehicleGroupResource(VehicleGroupService vehicleGroupService, VehicleGroupRepository vehicleGroupRepository) {
        this.vehicleGroupService = vehicleGroupService;
        this.vehicleGroupRepository = vehicleGroupRepository;
    }

    /**
     * {@code POST  /vehicle-groups} : Create a new vehicleGroup.
     *
     * @param vehicleGroupDTO the vehicleGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleGroupDTO, or with status {@code 400 (Bad Request)} if the vehicleGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleGroupDTO> createVehicleGroup(@Valid @RequestBody VehicleGroupDTO vehicleGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleGroup : {}", vehicleGroupDTO);
        if (vehicleGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleGroupDTO = vehicleGroupService.save(vehicleGroupDTO);
        return ResponseEntity.created(new URI("/api/vehicle-groups/" + vehicleGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleGroupDTO.getId().toString()))
            .body(vehicleGroupDTO);
    }

    /**
     * {@code PUT  /vehicle-groups/:id} : Updates an existing vehicleGroup.
     *
     * @param id the id of the vehicleGroupDTO to save.
     * @param vehicleGroupDTO the vehicleGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleGroupDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleGroupDTO> updateVehicleGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleGroupDTO vehicleGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleGroup : {}, {}", id, vehicleGroupDTO);
        if (vehicleGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleGroupDTO = vehicleGroupService.update(vehicleGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleGroupDTO.getId().toString()))
            .body(vehicleGroupDTO);
    }

    /**
     * {@code PATCH  /vehicle-groups/:id} : Partial updates given fields of an existing vehicleGroup, field will ignore if it is null
     *
     * @param id the id of the vehicleGroupDTO to save.
     * @param vehicleGroupDTO the vehicleGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleGroupDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleGroupDTO> partialUpdateVehicleGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleGroupDTO vehicleGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleGroup partially : {}, {}", id, vehicleGroupDTO);
        if (vehicleGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleGroupDTO> result = vehicleGroupService.partialUpdate(vehicleGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-groups} : get all the vehicleGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleGroupDTO>> getAllVehicleGroups(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VehicleGroups");
        Page<VehicleGroupDTO> page = vehicleGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-groups/:id} : get the "id" vehicleGroup.
     *
     * @param id the id of the vehicleGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleGroupDTO> getVehicleGroup(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleGroup : {}", id);
        Optional<VehicleGroupDTO> vehicleGroupDTO = vehicleGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleGroupDTO);
    }

    /**
     * {@code DELETE  /vehicle-groups/:id} : delete the "id" vehicleGroup.
     *
     * @param id the id of the vehicleGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleGroup(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleGroup : {}", id);
        vehicleGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
