package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.VehicleDamage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleDamage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleDamageRepository extends JpaRepository<VehicleDamage, Long> {}
