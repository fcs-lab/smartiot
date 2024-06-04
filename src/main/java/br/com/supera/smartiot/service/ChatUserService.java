package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.ChatUser;
import br.com.supera.smartiot.repository.ChatUserRepository;
import br.com.supera.smartiot.service.dto.ChatUserDTO;
import br.com.supera.smartiot.service.mapper.ChatUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.ChatUser}.
 */
@Service
@Transactional
public class ChatUserService {

    private final Logger log = LoggerFactory.getLogger(ChatUserService.class);

    private final ChatUserRepository chatUserRepository;

    private final ChatUserMapper chatUserMapper;

    public ChatUserService(ChatUserRepository chatUserRepository, ChatUserMapper chatUserMapper) {
        this.chatUserRepository = chatUserRepository;
        this.chatUserMapper = chatUserMapper;
    }

    /**
     * Save a chatUser.
     *
     * @param chatUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatUserDTO save(ChatUserDTO chatUserDTO) {
        log.debug("Request to save ChatUser : {}", chatUserDTO);
        ChatUser chatUser = chatUserMapper.toEntity(chatUserDTO);
        chatUser = chatUserRepository.save(chatUser);
        return chatUserMapper.toDto(chatUser);
    }

    /**
     * Update a chatUser.
     *
     * @param chatUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ChatUserDTO update(ChatUserDTO chatUserDTO) {
        log.debug("Request to update ChatUser : {}", chatUserDTO);
        ChatUser chatUser = chatUserMapper.toEntity(chatUserDTO);
        chatUser = chatUserRepository.save(chatUser);
        return chatUserMapper.toDto(chatUser);
    }

    /**
     * Partially update a chatUser.
     *
     * @param chatUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ChatUserDTO> partialUpdate(ChatUserDTO chatUserDTO) {
        log.debug("Request to partially update ChatUser : {}", chatUserDTO);

        return chatUserRepository
            .findById(chatUserDTO.getId())
            .map(existingChatUser -> {
                chatUserMapper.partialUpdate(existingChatUser, chatUserDTO);

                return existingChatUser;
            })
            .map(chatUserRepository::save)
            .map(chatUserMapper::toDto);
    }

    /**
     * Get all the chatUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChatUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChatUsers");
        return chatUserRepository.findAll(pageable).map(chatUserMapper::toDto);
    }

    /**
     * Get one chatUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChatUserDTO> findOne(Long id) {
        log.debug("Request to get ChatUser : {}", id);
        return chatUserRepository.findById(id).map(chatUserMapper::toDto);
    }

    /**
     * Delete the chatUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChatUser : {}", id);
        chatUserRepository.deleteById(id);
    }
}
