package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.VehicleInfoRepository;
import br.com.supera.smartiot.service.VehicleInfoService;
import br.com.supera.smartiot.service.dto.VehicleInfoDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.VehicleInfo}.
 */
@RestController
@RequestMapping("/api/vehicle-infos")
public class VehicleInfoResource {

    private final Logger log = LoggerFactory.getLogger(VehicleInfoResource.class);

    private static final String ENTITY_NAME = "vehicleInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleInfoService vehicleInfoService;

    private final VehicleInfoRepository vehicleInfoRepository;

    public VehicleInfoResource(VehicleInfoService vehicleInfoService, VehicleInfoRepository vehicleInfoRepository) {
        this.vehicleInfoService = vehicleInfoService;
        this.vehicleInfoRepository = vehicleInfoRepository;
    }

    /**
     * {@code POST  /vehicle-infos} : Create a new vehicleInfo.
     *
     * @param vehicleInfoDTO the vehicleInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleInfoDTO, or with status {@code 400 (Bad Request)} if the vehicleInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VehicleInfoDTO> createVehicleInfo(@Valid @RequestBody VehicleInfoDTO vehicleInfoDTO) throws URISyntaxException {
        log.debug("REST request to save VehicleInfo : {}", vehicleInfoDTO);
        if (vehicleInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vehicleInfoDTO = vehicleInfoService.save(vehicleInfoDTO);
        return ResponseEntity.created(new URI("/api/vehicle-infos/" + vehicleInfoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vehicleInfoDTO.getId().toString()))
            .body(vehicleInfoDTO);
    }

    /**
     * {@code PUT  /vehicle-infos/:id} : Updates an existing vehicleInfo.
     *
     * @param id the id of the vehicleInfoDTO to save.
     * @param vehicleInfoDTO the vehicleInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleInfoDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VehicleInfoDTO> updateVehicleInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VehicleInfoDTO vehicleInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleInfo : {}, {}", id, vehicleInfoDTO);
        if (vehicleInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vehicleInfoDTO = vehicleInfoService.update(vehicleInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleInfoDTO.getId().toString()))
            .body(vehicleInfoDTO);
    }

    /**
     * {@code PATCH  /vehicle-infos/:id} : Partial updates given fields of an existing vehicleInfo, field will ignore if it is null
     *
     * @param id the id of the vehicleInfoDTO to save.
     * @param vehicleInfoDTO the vehicleInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleInfoDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleInfoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleInfoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleInfoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleInfoDTO> partialUpdateVehicleInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VehicleInfoDTO vehicleInfoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleInfo partially : {}, {}", id, vehicleInfoDTO);
        if (vehicleInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleInfoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleInfoDTO> result = vehicleInfoService.partialUpdate(vehicleInfoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleInfoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-infos} : get all the vehicleInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleInfos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VehicleInfoDTO>> getAllVehicleInfos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VehicleInfos");
        Page<VehicleInfoDTO> page = vehicleInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-infos/:id} : get the "id" vehicleInfo.
     *
     * @param id the id of the vehicleInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VehicleInfoDTO> getVehicleInfo(@PathVariable("id") Long id) {
        log.debug("REST request to get VehicleInfo : {}", id);
        Optional<VehicleInfoDTO> vehicleInfoDTO = vehicleInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleInfoDTO);
    }

    /**
     * {@code DELETE  /vehicle-infos/:id} : delete the "id" vehicleInfo.
     *
     * @param id the id of the vehicleInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleInfo(@PathVariable("id") Long id) {
        log.debug("REST request to delete VehicleInfo : {}", id);
        vehicleInfoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
