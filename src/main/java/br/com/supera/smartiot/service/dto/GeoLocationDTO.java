package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.GeoLocation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GeoLocationDTO implements Serializable {

    private Long id;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String fullAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoLocationDTO)) {
            return false;
        }

        GeoLocationDTO geoLocationDTO = (GeoLocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, geoLocationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GeoLocationDTO{" +
            "id=" + getId() +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", fullAddress='" + getFullAddress() + "'" +
            "}";
    }
}
