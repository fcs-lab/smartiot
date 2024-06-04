package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.StorageBlobRepository;
import br.com.supera.smartiot.service.StorageBlobService;
import br.com.supera.smartiot.service.dto.StorageBlobDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.StorageBlob}.
 */
@RestController
@RequestMapping("/api/storage-blobs")
public class StorageBlobResource {

    private final Logger log = LoggerFactory.getLogger(StorageBlobResource.class);

    private static final String ENTITY_NAME = "storageBlob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageBlobService storageBlobService;

    private final StorageBlobRepository storageBlobRepository;

    public StorageBlobResource(StorageBlobService storageBlobService, StorageBlobRepository storageBlobRepository) {
        this.storageBlobService = storageBlobService;
        this.storageBlobRepository = storageBlobRepository;
    }

    /**
     * {@code POST  /storage-blobs} : Create a new storageBlob.
     *
     * @param storageBlobDTO the storageBlobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageBlobDTO, or with status {@code 400 (Bad Request)} if the storageBlob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StorageBlobDTO> createStorageBlob(@Valid @RequestBody StorageBlobDTO storageBlobDTO) throws URISyntaxException {
        log.debug("REST request to save StorageBlob : {}", storageBlobDTO);
        if (storageBlobDTO.getId() != null) {
            throw new BadRequestAlertException("A new storageBlob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        storageBlobDTO = storageBlobService.save(storageBlobDTO);
        return ResponseEntity.created(new URI("/api/storage-blobs/" + storageBlobDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, storageBlobDTO.getId().toString()))
            .body(storageBlobDTO);
    }

    /**
     * {@code PUT  /storage-blobs/:id} : Updates an existing storageBlob.
     *
     * @param id the id of the storageBlobDTO to save.
     * @param storageBlobDTO the storageBlobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlobDTO,
     * or with status {@code 400 (Bad Request)} if the storageBlobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageBlobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageBlobDTO> updateStorageBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StorageBlobDTO storageBlobDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StorageBlob : {}, {}", id, storageBlobDTO);
        if (storageBlobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        storageBlobDTO = storageBlobService.update(storageBlobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlobDTO.getId().toString()))
            .body(storageBlobDTO);
    }

    /**
     * {@code PATCH  /storage-blobs/:id} : Partial updates given fields of an existing storageBlob, field will ignore if it is null
     *
     * @param id the id of the storageBlobDTO to save.
     * @param storageBlobDTO the storageBlobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageBlobDTO,
     * or with status {@code 400 (Bad Request)} if the storageBlobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageBlobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageBlobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageBlobDTO> partialUpdateStorageBlob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StorageBlobDTO storageBlobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StorageBlob partially : {}, {}", id, storageBlobDTO);
        if (storageBlobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageBlobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageBlobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageBlobDTO> result = storageBlobService.partialUpdate(storageBlobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageBlobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-blobs} : get all the storageBlobs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageBlobs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StorageBlobDTO>> getAllStorageBlobs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StorageBlobs");
        Page<StorageBlobDTO> page = storageBlobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storage-blobs/:id} : get the "id" storageBlob.
     *
     * @param id the id of the storageBlobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageBlobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageBlobDTO> getStorageBlob(@PathVariable("id") Long id) {
        log.debug("REST request to get StorageBlob : {}", id);
        Optional<StorageBlobDTO> storageBlobDTO = storageBlobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageBlobDTO);
    }

    /**
     * {@code DELETE  /storage-blobs/:id} : delete the "id" storageBlob.
     *
     * @param id the id of the storageBlobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageBlob(@PathVariable("id") Long id) {
        log.debug("REST request to delete StorageBlob : {}", id);
        storageBlobService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
