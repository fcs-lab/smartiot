package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleModelRepository;
import br.com.supera.smartiot.service.VehicleModelService;
import br.com.supera.smartiot.service.dto.VehicleModelDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleModel}.
 */
@RestController
@RequestMapping("/api/vehicle-models")
public class VehicleModelResource {

    private final Logger log = LoggerFactory.getLogger(VehicleModelResource.class);

    private static final String ENTITY_NAME = "vehicleModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleModelService vehicleModelService;

    private final VehicleModelRepository vehicleModelRepository;

    public VehicleModelResource(VehicleModelService vehicleModelService, VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelService = vehicleModelService;
        this.vehicleModelRepository = vehicleModelRepository;
    }

    /**
     * {@code POST  /vehicle-models} : Create a new vehicleModel.
     *
     * @param vehicleModelDTO the vehicleModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleModelDTO, or with status {@code 400 (Bad Request)} if the vehicleModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleModelDTO> createVehicleModel(@Valid @RequestBody VehicleModelDTO vehicleModelDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleModel : {}", vehicleModelDTO);
        if (vehicleModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleModelDTO = vehicleModelService.save(vehicleModelDTO);
        return ResponseEntity.created(new URI("/api/vehicle-models/" + vehicleModelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleModelDTO.getId().toString()))
            .body(vehicleModelDTO);
    }

    /**
     * {@code PUT  /vehicle-models/:id} : Updates an existing vehicleModel.
     *
     * @param id the id of the vehicleModelDTO to save.
     * @param vehicleModelDTO the vehicleModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleModelDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleModelDTO> updateVehicleModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleModelDTO vehicleModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleModel : {}, {}", id, vehicleModelDTO);
        if (vehicleModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleModelDTO = vehicleModelService.update(vehicleModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleModelDTO.getId().toString()))
            .body(vehicleModelDTO);
    }

    /**
     * {@code PATCH  /vehicle-models/:id} : Partial updates given fields of an existing vehicleModel, field will ignore if it is null
     *
     * @param id the id of the vehicleModelDTO to save.
     * @param vehicleModelDTO the vehicleModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleModelDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleModelDTO> partialUpdateVehicleModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleModelDTO vehicleModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleModel partially : {}, {}", id, vehicleModelDTO);
        if (vehicleModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleModelDTO> result = vehicleModelService.partialUpdate(vehicleModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-models} : get all the vehicleModels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleModels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleModelDTO>> getAllVehicleModels(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VehicleModels");
        Page<VehicleModelDTO> page = vehicleModelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-models/:id} : get the "id" vehicleModel.
     *
     * @param id the id of the vehicleModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleModelDTO> getVehicleModel(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleModel : {}", id);
        Optional<VehicleModelDTO> vehicleModelDTO = vehicleModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleModelDTO);
    }

    /**
     * {@code DELETE  /vehicle-models/:id} : delete the "id" vehicleModel.
     *
     * @param id the id of the vehicleModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleModel(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleModel : {}", id);
        vehicleModelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
