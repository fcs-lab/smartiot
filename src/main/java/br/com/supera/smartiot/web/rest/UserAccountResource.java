package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.UserAccountRepository;
import br.com.supera.smartiot.service.UserAccountService;
import br.com.supera.smartiot.service.dto.UserAccountDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.UserAccount}.
 */
@RestController
@RequestMapping("/api/user-accounts")
public class UserAccountResource {

    private final Logger log = LoggerFactory.getLogger(UserAccountResource.class);

    private static final String ENTITY_NAME = "userAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAccountService userAccountService;

    private final UserAccountRepository userAccountRepository;

    public UserAccountResource(UserAccountService userAccountService, UserAccountRepository userAccountRepository) {
        this.userAccountService = userAccountService;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * {@code POST  /user-accounts} : Create a new userAccount.
     *
     * @param userAccountDTO the userAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAccountDTO, or with status {@code 400 (Bad Request)} if the userAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAccountDTO> createUserAccount(@Valid @RequestBody UserAccountDTO userAccountDTO) throws URISyntaxException {
        log.debug("REST request to save UserAccount : {}", userAccountDTO);
        if (userAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new userAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAccountDTO = userAccountService.save(userAccountDTO);
        return ResponseEntity.created(new URI("/api/user-accounts/" + userAccountDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAccountDTO.getId().toString()))
            .body(userAccountDTO);
    }

    /**
     * {@code PUT  /user-accounts/:id} : Updates an existing userAccount.
     *
     * @param id the id of the userAccountDTO to save.
     * @param userAccountDTO the userAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccountDTO,
     * or with status {@code 400 (Bad Request)} if the userAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAccountDTO> updateUserAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAccountDTO userAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserAccount : {}, {}", id, userAccountDTO);
        if (userAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAccountDTO = userAccountService.update(userAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccountDTO.getId().toString()))
            .body(userAccountDTO);
    }

    /**
     * {@code PATCH  /user-accounts/:id} : Partial updates given fields of an existing userAccount, field will ignore if it is null
     *
     * @param id the id of the userAccountDTO to save.
     * @param userAccountDTO the userAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAccountDTO,
     * or with status {@code 400 (Bad Request)} if the userAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAccountDTO> partialUpdateUserAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAccountDTO userAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAccount partially : {}, {}", id, userAccountDTO);
        if (userAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAccountDTO> result = userAccountService.partialUpdate(userAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-accounts} : get all the userAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserAccountDTO>> getAllUserAccounts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserAccounts");
        Page<UserAccountDTO> page = userAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-accounts/:id} : get the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAccountDTO> getUserAccount(@PathVariable("id") Long id) {
        log.debug("REST request to get UserAccount : {}", id);
        Optional<UserAccountDTO> userAccountDTO = userAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAccountDTO);
    }

    /**
     * {@code DELETE  /user-accounts/:id} : delete the "id" userAccount.
     *
     * @param id the id of the userAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAccount(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserAccount : {}", id);
        userAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
