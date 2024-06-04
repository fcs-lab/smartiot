package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.UserContractRepository;
import br.com.supera.smartiot.service.UserContractService;
import br.com.supera.smartiot.service.dto.UserContractDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.UserContract}.
 */
@RestController
@RequestMapping("/api/user-contracts")
public class UserContractResource {

    private final Logger log = LoggerFactory.getLogger(UserContractResource.class);

    private static final String ENTITY_NAME = "userContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserContractService userContractService;

    private final UserContractRepository userContractRepository;

    public UserContractResource(UserContractService userContractService, UserContractRepository userContractRepository) {
        this.userContractService = userContractService;
        this.userContractRepository = userContractRepository;
    }

    /**
     * {@code POST  /user-contracts} : Create a new userContract.
     *
     * @param userContractDTO the userContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userContractDTO, or with status {@code 400 (Bad Request)} if the userContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserContractDTO> createUserContract(@Valid @RequestBody UserContractDTO userContractDTO)
        throws URISyntaxException {
        log.debug("REST request to save UserContract : {}", userContractDTO);
        if (userContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new userContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userContractDTO = userContractService.save(userContractDTO);
        return ResponseEntity.created(new URI("/api/user-contracts/" + userContractDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userContractDTO.getId().toString()))
            .body(userContractDTO);
    }

    /**
     * {@code PUT  /user-contracts/:id} : Updates an existing userContract.
     *
     * @param id the id of the userContractDTO to save.
     * @param userContractDTO the userContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userContractDTO,
     * or with status {@code 400 (Bad Request)} if the userContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserContractDTO> updateUserContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserContractDTO userContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserContract : {}, {}", id, userContractDTO);
        if (userContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userContractDTO = userContractService.update(userContractDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userContractDTO.getId().toString()))
            .body(userContractDTO);
    }

    /**
     * {@code PATCH  /user-contracts/:id} : Partial updates given fields of an existing userContract, field will ignore if it is null
     *
     * @param id the id of the userContractDTO to save.
     * @param userContractDTO the userContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userContractDTO,
     * or with status {@code 400 (Bad Request)} if the userContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserContractDTO> partialUpdateUserContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserContractDTO userContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserContract partially : {}, {}", id, userContractDTO);
        if (userContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserContractDTO> result = userContractService.partialUpdate(userContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userContractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-contracts} : get all the userContracts.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userContracts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserContractDTO>> getAllUserContracts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserContracts");
        Page<UserContractDTO> page;
        if (eagerload) {
            page = userContractService.findAllWithEagerRelationships(pageable);
        } else {
            page = userContractService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-contracts/:id} : get the "id" userContract.
     *
     * @param id the id of the userContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserContractDTO> getUserContract(@PathVariable("id") Long id) {
        log.debug("REST request to get UserContract : {}", id);
        Optional<UserContractDTO> userContractDTO = userContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userContractDTO);
    }

    /**
     * {@code DELETE  /user-contracts/:id} : delete the "id" userContract.
     *
     * @param id the id of the userContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserContract(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserContract : {}", id);
        userContractService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
