package br.com.supera.smartiot.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link br.com.supera.smartiot.domain.ChatBooking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatBookingDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant bookingTimestamp;

    private ChatSessionDTO session;

    private VehicleInfoDTO vehicle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBookingTimestamp() {
        return bookingTimestamp;
    }

    public void setBookingTimestamp(Instant bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }

    public ChatSessionDTO getSession() {
        return session;
    }

    public void setSession(ChatSessionDTO session) {
        this.session = session;
    }

    public VehicleInfoDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleInfoDTO vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatBookingDTO)) {
            return false;
        }

        ChatBookingDTO chatBookingDTO = (ChatBookingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, chatBookingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatBookingDTO{" +
            "id=" + getId() +
            ", bookingTimestamp='" + getBookingTimestamp() + "'" +
            ", session=" + getSession() +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
