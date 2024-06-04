package br.com.supera.smartiot.service;

import br.com.supera.smartiot.domain.StorageBlob;
import br.com.supera.smartiot.repository.StorageBlobRepository;
import br.com.supera.smartiot.service.dto.StorageBlobDTO;
import br.com.supera.smartiot.service.mapper.StorageBlobMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link br.com.supera.smartiot.domain.StorageBlob}.
 */
@Service
@Transactional
public class StorageBlobService {

    private final Logger log = LoggerFactory.getLogger(StorageBlobService.class);

    private final StorageBlobRepository storageBlobRepository;

    private final StorageBlobMapper storageBlobMapper;

    public StorageBlobService(StorageBlobRepository storageBlobRepository, StorageBlobMapper storageBlobMapper) {
        this.storageBlobRepository = storageBlobRepository;
        this.storageBlobMapper = storageBlobMapper;
    }

    /**
     * Save a storageBlob.
     *
     * @param storageBlobDTO the entity to save.
     * @return the persisted entity.
     */
    public StorageBlobDTO save(StorageBlobDTO storageBlobDTO) {
        log.debug("Request to save StorageBlob : {}", storageBlobDTO);
        StorageBlob storageBlob = storageBlobMapper.toEntity(storageBlobDTO);
        storageBlob = storageBlobRepository.save(storageBlob);
        return storageBlobMapper.toDto(storageBlob);
    }

    /**
     * Update a storageBlob.
     *
     * @param storageBlobDTO the entity to save.
     * @return the persisted entity.
     */
    public StorageBlobDTO update(StorageBlobDTO storageBlobDTO) {
        log.debug("Request to update StorageBlob : {}", storageBlobDTO);
        StorageBlob storageBlob = storageBlobMapper.toEntity(storageBlobDTO);
        storageBlob = storageBlobRepository.save(storageBlob);
        return storageBlobMapper.toDto(storageBlob);
    }

    /**
     * Partially update a storageBlob.
     *
     * @param storageBlobDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StorageBlobDTO> partialUpdate(StorageBlobDTO storageBlobDTO) {
        log.debug("Request to partially update StorageBlob : {}", storageBlobDTO);

        return storageBlobRepository
            .findById(storageBlobDTO.getId())
            .map(existingStorageBlob -> {
                storageBlobMapper.partialUpdate(existingStorageBlob, storageBlobDTO);

                return existingStorageBlob;
            })
            .map(storageBlobRepository::save)
            .map(storageBlobMapper::toDto);
    }

    /**
     * Get all the storageBlobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StorageBlobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StorageBlobs");
        return storageBlobRepository.findAll(pageable).map(storageBlobMapper::toDto);
    }

    /**
     * Get one storageBlob by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StorageBlobDTO> findOne(Long id) {
        log.debug("Request to get StorageBlob : {}", id);
        return storageBlobRepository.findById(id).map(storageBlobMapper::toDto);
    }

    /**
     * Delete the storageBlob by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StorageBlob : {}", id);
        storageBlobRepository.deleteById(id);
    }
}
