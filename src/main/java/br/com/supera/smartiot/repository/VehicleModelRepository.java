package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {}
