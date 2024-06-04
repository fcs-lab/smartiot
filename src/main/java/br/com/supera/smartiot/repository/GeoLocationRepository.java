package br.com.supera.smartiot.repository;

import br.com.supera.smartiot.domain.GeoLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GeoLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GeoLocationRepository extends JpaRepository<GeoLocation, Long> {}
