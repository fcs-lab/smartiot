package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleSubStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleSubStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleSubStatusRepository extends JpaRepository<VehicleSubStatus, Long> {}
