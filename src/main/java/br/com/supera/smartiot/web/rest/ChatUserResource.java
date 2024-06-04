package br.com.supera.smartiot.web.rest;

import br.com.supera.smartiot.repository.ChatUserRepository;
import br.com.supera.smartiot.service.ChatUserService;
import br.com.supera.smartiot.service.dto.ChatUserDTO;
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
 * REST controller for managing {@link br.com.supera.smartiot.domain.ChatUser}.
 */
@RestController
@RequestMapping("/api/chat-users")
public class ChatUserResource {

    private final Logger log = LoggerFactory.getLogger(ChatUserResource.class);

    private static final String ENTITY_NAME = "chatUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChatUserService chatUserService;

    private final ChatUserRepository chatUserRepository;

    public ChatUserResource(ChatUserService chatUserService, ChatUserRepository chatUserRepository) {
        this.chatUserService = chatUserService;
        this.chatUserRepository = chatUserRepository;
    }

    /**
     * {@code POST  /chat-users} : Create a new chatUser.
     *
     * @param chatUserDTO the chatUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chatUserDTO, or with status {@code 400 (Bad Request)} if the chatUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ChatUserDTO> createChatUser(@Valid @RequestBody ChatUserDTO chatUserDTO) throws URISyntaxException {
        log.debug("REST request to save ChatUser : {}", chatUserDTO);
        if (chatUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new chatUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        chatUserDTO = chatUserService.save(chatUserDTO);
        return ResponseEntity.created(new URI("/api/chat-users/" + chatUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, chatUserDTO.getId().toString()))
            .body(chatUserDTO);
    }

    /**
     * {@code PUT  /chat-users/:id} : Updates an existing chatUser.
     *
     * @param id the id of the chatUserDTO to save.
     * @param chatUserDTO the chatUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chatUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChatUserDTO> updateChatUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChatUserDTO chatUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChatUser : {}, {}", id, chatUserDTO);
        if (chatUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        chatUserDTO = chatUserService.update(chatUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatUserDTO.getId().toString()))
            .body(chatUserDTO);
    }

    /**
     * {@code PATCH  /chat-users/:id} : Partial updates given fields of an existing chatUser, field will ignore if it is null
     *
     * @param id the id of the chatUserDTO to save.
     * @param chatUserDTO the chatUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chatUserDTO,
     * or with status {@code 400 (Bad Request)} if the chatUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chatUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chatUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChatUserDTO> partialUpdateChatUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChatUserDTO chatUserDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChatUser partially : {}, {}", id, chatUserDTO);
        if (chatUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chatUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chatUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChatUserDTO> result = chatUserService.partialUpdate(chatUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chatUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chat-users} : get all the chatUsers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chatUsers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ChatUserDTO>> getAllChatUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ChatUsers");
        Page<ChatUserDTO> page = chatUserService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chat-users/:id} : get the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chatUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChatUserDTO> getChatUser(@PathVariable("id") Long id) {
        log.debug("REST request to get ChatUser : {}", id);
        Optional<ChatUserDTO> chatUserDTO = chatUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chatUserDTO);
    }

    /**
     * {@code DELETE  /chat-users/:id} : delete the "id" chatUser.
     *
     * @param id the id of the chatUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatUser(@PathVariable("id") Long id) {
        log.debug("REST request to delete ChatUser : {}", id);
        chatUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
