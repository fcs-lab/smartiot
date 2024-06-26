package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.PricingRepository;
import br.com.supera.smartiot.service.PricingService;
import br.com.supera.smartiot.service.dto.PricingDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.Pricing}.
 */
@RestController
@RequestMapping("/api/pricings")
public class PricingResource {

    private final Logger log = LoggerFactory.getLogger(PricingResource.class);

    private static final String ENTITY_NAME = "pricing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricingService pricingService;

    private final PricingRepository pricingRepository;

    public PricingResource(PricingService pricingService, PricingRepository pricingRepository) {
        this.pricingService = pricingService;
        this.pricingRepository = pricingRepository;
    }

    /**
     * {@code POST  /pricings} : Create a new pricing.
     *
     * @param pricingDTO the pricingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricingDTO, or with status {@code 400 (Bad Request)} if the pricing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PricingDTO> createPricing(@Valid @RequestBody PricingDTO pricingDTO) throws URISyntaxException {
        log.debug("REST request to save Pricing : {}", pricingDTO);
        if (pricingDTO.getId() != null) {
            throw new BadRequestAlertException("A new pricing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pricingDTO = pricingService.save(pricingDTO);
        return ResponseEntity.created(new URI("/api/pricings/" + pricingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pricingDTO.getId().toString()))
            .body(pricingDTO);
    }

    /**
     * {@code PUT  /pricings/:id} : Updates an existing pricing.
     *
     * @param id the id of the pricingDTO to save.
     * @param pricingDTO the pricingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricingDTO,
     * or with status {@code 400 (Bad Request)} if the pricingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PricingDTO> updatePricing(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PricingDTO pricingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pricing : {}, {}", id, pricingDTO);
        if (pricingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pricingDTO = pricingService.update(pricingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pricingDTO.getId().toString()))
            .body(pricingDTO);
    }

    /**
     * {@code PATCH  /pricings/:id} : Partial updates given fields of an existing pricing, field will ignore if it is null
     *
     * @param id the id of the pricingDTO to save.
     * @param pricingDTO the pricingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricingDTO,
     * or with status {@code 400 (Bad Request)} if the pricingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pricingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pricingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PricingDTO> partialUpdatePricing(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PricingDTO pricingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pricing partially : {}, {}", id, pricingDTO);
        if (pricingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pricingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pricingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PricingDTO> result = pricingService.partialUpdate(pricingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pricingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pricings} : get all the pricings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PricingDTO>> getAllPricings(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Pricings");
        Page<PricingDTO> page = pricingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pricings/:id} : get the "id" pricing.
     *
     * @param id the id of the pricingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PricingDTO> getPricing(@PathVariable("id") Long id) {
        log.debug("REST request to get Pricing : {}", id);
        Optional<PricingDTO> pricingDTO = pricingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pricingDTO);
    }

    /**
     * {@code DELETE  /pricings/:id} : delete the "id" pricing.
     *
     * @param id the id of the pricingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable("id") Long id) {
        log.debug("REST request to delete Pricing : {}", id);
        pricingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
