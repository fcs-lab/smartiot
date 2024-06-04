package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.CarRide;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CarRide entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRideRepository extends JpaRepository<CarRide, Long> {}
