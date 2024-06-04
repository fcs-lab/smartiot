package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleServiceRepository extends JpaRepository<VehicleService, Long> {}
