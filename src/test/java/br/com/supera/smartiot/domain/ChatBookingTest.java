package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ChatBookingTestSamples.*;
import static br.com.supera.smartiot.domain.ChatSessionTestSamples.*;
import static br.com.supera.smartiot.domain.VehicleInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatBookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatBooking.class);
        ChatBooking chatBooking1 = getChatBookingSample1();
        ChatBooking chatBooking2 = new ChatBooking();
        assertThat(chatBooking1).isNotEqualTo(chatBooking2);

        chatBooking2.setId(chatBooking1.getId());
        assertThat(chatBooking1).isEqualTo(chatBooking2);

        chatBooking2 = getChatBookingSample2();
        assertThat(chatBooking1).isNotEqualTo(chatBooking2);
    }

    @Test
    void sessionTest() {
        ChatBooking chatBooking = getChatBookingRandomSampleGenerator();
        ChatSession chatSessionBack = getChatSessionRandomSampleGenerator();

        chatBooking.setSession(chatSessionBack);
        assertThat(chatBooking.getSession()).isEqualTo(chatSessionBack);

        chatBooking.session(null);
        assertThat(chatBooking.getSession()).isNull();
    }

    @Test
    void vehicleTest() {
        ChatBooking chatBooking = getChatBookingRandomSampleGenerator();
        VehicleInfo vehicleInfoBack = getVehicleInfoRandomSampleGenerator();

        chatBooking.setVehicle(vehicleInfoBack);
        assertThat(chatBooking.getVehicle()).isEqualTo(vehicleInfoBack);

        chatBooking.vehicle(null);
        assertThat(chatBooking.getVehicle()).isNull();
    }
}
