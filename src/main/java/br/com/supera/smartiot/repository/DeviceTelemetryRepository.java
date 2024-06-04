package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.DeviceTelemetry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeviceTelemetry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceTelemetryRepository extends JpaRepository<DeviceTelemetry, Long> {}
