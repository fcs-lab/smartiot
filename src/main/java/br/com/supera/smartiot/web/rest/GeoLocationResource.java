package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.GeoLocationRepository;
import br.com.supera.smartiot.service.GeoLocationService;
import br.com.supera.smartiot.service.dto.GeoLocationDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.GeoLocation}.
 */
@RestController
@RequestMapping("/api/geo-locations")
public class GeoLocationResource {

    private final Logger log = LoggerFactory.getLogger(GeoLocationResource.class);

    private static final String ENTITY_NAME = "geoLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeoLocationService geoLocationService;

    private final GeoLocationRepository geoLocationRepository;

    public GeoLocationResource(GeoLocationService geoLocationService, GeoLocationRepository geoLocationRepository) {
        this.geoLocationService = geoLocationService;
        this.geoLocationRepository = geoLocationRepository;
    }

    /**
     * {@code POST  /geo-locations} : Create a new geoLocation.
     *
     * @param geoLocationDTO the geoLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geoLocationDTO, or with status {@code 400 (Bad Request)} if the geoLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GeoLocationDTO> createGeoLocation(@Valid @RequestBody GeoLocationDTO geoLocationDTO) throws URISyntaxException {
        log.debug("REST request to save GeoLocation : {}", geoLocationDTO);
        if (geoLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new geoLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        geoLocationDTO = geoLocationService.save(geoLocationDTO);
        return ResponseEntity.created(new URI("/api/geo-locations/" + geoLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, geoLocationDTO.getId().toString()))
            .body(geoLocationDTO);
    }

    /**
     * {@code PUT  /geo-locations/:id} : Updates an existing geoLocation.
     *
     * @param id the id of the geoLocationDTO to save.
     * @param geoLocationDTO the geoLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoLocationDTO,
     * or with status {@code 400 (Bad Request)} if the geoLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geoLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GeoLocationDTO> updateGeoLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GeoLocationDTO geoLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GeoLocation : {}, {}", id, geoLocationDTO);
        if (geoLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        geoLocationDTO = geoLocationService.update(geoLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoLocationDTO.getId().toString()))
            .body(geoLocationDTO);
    }

    /**
     * {@code PATCH  /geo-locations/:id} : Partial updates given fields of an existing geoLocation, field will ignore if it is null
     *
     * @param id the id of the geoLocationDTO to save.
     * @param geoLocationDTO the geoLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geoLocationDTO,
     * or with status {@code 400 (Bad Request)} if the geoLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the geoLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the geoLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GeoLocationDTO> partialUpdateGeoLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GeoLocationDTO geoLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GeoLocation partially : {}, {}", id, geoLocationDTO);
        if (geoLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, geoLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!geoLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GeoLocationDTO> result = geoLocationService.partialUpdate(geoLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geoLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /geo-locations} : get all the geoLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of geoLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GeoLocationDTO>> getAllGeoLocations(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of GeoLocations");
        Page<GeoLocationDTO> page = geoLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /geo-locations/:id} : get the "id" geoLocation.
     *
     * @param id the id of the geoLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geoLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GeoLocationDTO> getGeoLocation(@PathVariable("id") Long id) {
        log.debug("REST request to get GeoLocation : {}", id);
        Optional<GeoLocationDTO> geoLocationDTO = geoLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geoLocationDTO);
    }

    /**
     * {@code DELETE  /geo-locations/:id} : delete the "id" geoLocation.
     *
     * @param id the id of the geoLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGeoLocation(@PathVariable("id") Long id) {
        log.debug("REST request to delete GeoLocation : {}", id);
        geoLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
