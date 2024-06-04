package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleStatusLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleStatusLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleStatusLogRepository extends JpaRepository<VehicleStatusLog, Long> {}
