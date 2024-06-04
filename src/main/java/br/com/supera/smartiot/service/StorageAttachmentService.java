package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.StorageAttachment;
import br.com.supera.smartiot.repository.StorageAttachmentRepository;
import br.com.supera.smartiot.service.dto.StorageAttachmentDTO;
import br.com.supera.smartiot.service.mapper.StorageAttachmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.StorageAttachment}.
 */
@Service
@Transactional
public class StorageAttachmentService {

    private final Logger log = LoggerFactory.getLogger(StorageAttachmentService.class);

    private final StorageAttachmentRepository storageAttachmentRepository;

    private final StorageAttachmentMapper storageAttachmentMapper;

    public StorageAttachmentService(
        StorageAttachmentRepository storageAttachmentRepository,
        StorageAttachmentMapper storageAttachmentMapper
    ) {
        this.storageAttachmentRepository = storageAttachmentRepository;
        this.storageAttachmentMapper = storageAttachmentMapper;
    }

    /**
     * Save a storageAttachment.
     *
     * @param storageAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public StorageAttachmentDTO save(StorageAttachmentDTO storageAttachmentDTO) {
        log.debug("Request to save StorageAttachment : {}", storageAttachmentDTO);
        StorageAttachment storageAttachment = storageAttachmentMapper.toEntity(storageAttachmentDTO);
        storageAttachment = storageAttachmentRepository.save(storageAttachment);
        return storageAttachmentMapper.toDto(storageAttachment);
    }

    /**
     * Update a storageAttachment.
     *
     * @param storageAttachmentDTO the entity to save.
     * @return the persisted entity.
     */
    public StorageAttachmentDTO update(StorageAttachmentDTO storageAttachmentDTO) {
        log.debug("Request to update StorageAttachment : {}", storageAttachmentDTO);
        StorageAttachment storageAttachment = storageAttachmentMapper.toEntity(storageAttachmentDTO);
        storageAttachment = storageAttachmentRepository.save(storageAttachment);
        return storageAttachmentMapper.toDto(storageAttachment);
    }

    /**
     * Partially update a storageAttachment.
     *
     * @param storageAttachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StorageAttachmentDTO> partialUpdate(StorageAttachmentDTO storageAttachmentDTO) {
        log.debug("Request to partially update StorageAttachment : {}", storageAttachmentDTO);

        return storageAttachmentRepository
            .findById(storageAttachmentDTO.getId())
            .map(existingStorageAttachment -> {
                storageAttachmentMapper.partialUpdate(existingStorageAttachment, storageAttachmentDTO);

                return existingStorageAttachment;
            })
            .map(storageAttachmentRepository::save)
            .map(storageAttachmentMapper::toDto);
    }

    /**
     * Get all the storageAttachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageAttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageAttachments");
        return storageAttachmentRepository.findAll(pageable).map(storageAttachmentMapper::toDto);
    }

    /**
     * Get one storageAttachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StorageAttachmentDTO> findOne(Long id) {
        log.debug("Request to get StorageAttachment : {}", id);
        return storageAttachmentRepository.findById(id).map(storageAttachmentMapper::toDto);
    }

    /**
     * Delete the storageAttachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StorageAttachment : {}", id);
        storageAttachmentRepository.deleteById(id);
    }
}
