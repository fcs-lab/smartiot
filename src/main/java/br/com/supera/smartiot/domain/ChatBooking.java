package br.com.supera.smartiot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChatBooking.
 */
@Entity
@Table(name = "chat_booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChatBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "booking_timestamp", nullable = false)
    private Instant bookingTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "messages" }, allowSetters = true)
    private ChatSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "damages", "reservations", "services", "alerts", "devices" }, allowSetters = true)
    private VehicleInfo vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChatBooking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBookingTimestamp() {
        return this.bookingTimestamp;
    }

    public ChatBooking bookingTimestamp(Instant bookingTimestamp) {
        this.setBookingTimestamp(bookingTimestamp);
        return this;
    }

    public void setBookingTimestamp(Instant bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }

    public ChatSession getSession() {
        return this.session;
    }

    public void setSession(ChatSession chatSession) {
        this.session = chatSession;
    }

    public ChatBooking session(ChatSession chatSession) {
        this.setSession(chatSession);
        return this;
    }

    public VehicleInfo getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(VehicleInfo vehicleInfo) {
        this.vehicle = vehicleInfo;
    }

    public ChatBooking vehicle(VehicleInfo vehicleInfo) {
        this.setVehicle(vehicleInfo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChatBooking)) {
            return false;
        }
        return getId() != null && getId().equals(((ChatBooking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChatBooking{" +
            "id=" + getId() +
            ", bookingTimestamp='" + getBookingTimestamp() + "'" +
            "}";
    }
}
