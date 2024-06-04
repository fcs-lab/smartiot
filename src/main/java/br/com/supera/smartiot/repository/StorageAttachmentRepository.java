package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.StorageAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StorageAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageAttachmentRepository extends JpaRepository<StorageAttachment, Long> {}
