package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.StorageAttachmentRepository;
import br.com.supera.smartiot.service.StorageAttachmentService;
import br.com.supera.smartiot.service.dto.StorageAttachmentDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.StorageAttachment}.
 */
@RestController
@RequestMapping("/api/storage-attachments")
public class StorageAttachmentResource {

    private final Logger log = LoggerFactory.getLogger(StorageAttachmentResource.class);

    private static final String ENTITY_NAME = "storageAttachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StorageAttachmentService storageAttachmentService;

    private final StorageAttachmentRepository storageAttachmentRepository;

    public StorageAttachmentResource(
        StorageAttachmentService storageAttachmentService,
        StorageAttachmentRepository storageAttachmentRepository
    ) {
        this.storageAttachmentService = storageAttachmentService;
        this.storageAttachmentRepository = storageAttachmentRepository;
    }

    /**
     * {@code POST  /storage-attachments} : Create a new storageAttachment.
     *
     * @param storageAttachmentDTO the storageAttachmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new storageAttachmentDTO, or with status {@code 400 (Bad Request)} if the storageAttachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StorageAttachmentDTO> createStorageAttachment(@Valid @RequestBody StorageAttachmentDTO storageAttachmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save StorageAttachment : {}", storageAttachmentDTO);
        if (storageAttachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new storageAttachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        storageAttachmentDTO = storageAttachmentService.save(storageAttachmentDTO);
        return ResponseEntity.created(new URI("/api/storage-attachments/" + storageAttachmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, storageAttachmentDTO.getId().toString()))
            .body(storageAttachmentDTO);
    }

    /**
     * {@code PUT  /storage-attachments/:id} : Updates an existing storageAttachment.
     *
     * @param id the id of the storageAttachmentDTO to save.
     * @param storageAttachmentDTO the storageAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the storageAttachmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the storageAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StorageAttachmentDTO> updateStorageAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StorageAttachmentDTO storageAttachmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StorageAttachment : {}, {}", id, storageAttachmentDTO);
        if (storageAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        storageAttachmentDTO = storageAttachmentService.update(storageAttachmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageAttachmentDTO.getId().toString()))
            .body(storageAttachmentDTO);
    }

    /**
     * {@code PATCH  /storage-attachments/:id} : Partial updates given fields of an existing storageAttachment, field will ignore if it is null
     *
     * @param id the id of the storageAttachmentDTO to save.
     * @param storageAttachmentDTO the storageAttachmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated storageAttachmentDTO,
     * or with status {@code 400 (Bad Request)} if the storageAttachmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the storageAttachmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the storageAttachmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StorageAttachmentDTO> partialUpdateStorageAttachment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StorageAttachmentDTO storageAttachmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StorageAttachment partially : {}, {}", id, storageAttachmentDTO);
        if (storageAttachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, storageAttachmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!storageAttachmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StorageAttachmentDTO> result = storageAttachmentService.partialUpdate(storageAttachmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, storageAttachmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /storage-attachments} : get all the storageAttachments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of storageAttachments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StorageAttachmentDTO>> getAllStorageAttachments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of StorageAttachments");
        Page<StorageAttachmentDTO> page = storageAttachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /storage-attachments/:id} : get the "id" storageAttachment.
     *
     * @param id the id of the storageAttachmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the storageAttachmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StorageAttachmentDTO> getStorageAttachment(@PathVariable("id") Long id) {
        log.debug("REST request to get StorageAttachment : {}", id);
        Optional<StorageAttachmentDTO> storageAttachmentDTO = storageAttachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(storageAttachmentDTO);
    }

    /**
     * {@code DELETE  /storage-attachments/:id} : delete the "id" storageAttachment.
     *
     * @param id the id of the storageAttachmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStorageAttachment(@PathVariable("id") Long id) {
        log.debug("REST request to delete StorageAttachment : {}", id);
        storageAttachmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
