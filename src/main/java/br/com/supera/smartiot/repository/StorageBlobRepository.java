package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.StorageBlob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StorageBlob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageBlobRepository extends JpaRepository<StorageBlob, Long> {}
