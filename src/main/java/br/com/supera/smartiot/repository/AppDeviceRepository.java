package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.AppDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppDeviceRepository extends JpaRepository<AppDevice, Long> {}
