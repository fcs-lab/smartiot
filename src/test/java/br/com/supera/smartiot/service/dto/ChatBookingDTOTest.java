package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatBookingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatBookingDTO.class);
        ChatBookingDTO chatBookingDTO1 = new ChatBookingDTO();
        chatBookingDTO1.setId(1L);
        ChatBookingDTO chatBookingDTO2 = new ChatBookingDTO();
        assertThat(chatBookingDTO1).isNotEqualTo(chatBookingDTO2);
        chatBookingDTO2.setId(chatBookingDTO1.getId());
        assertThat(chatBookingDTO1).isEqualTo(chatBookingDTO2);
        chatBookingDTO2.setId(2L);
        assertThat(chatBookingDTO1).isNotEqualTo(chatBookingDTO2);
        chatBookingDTO1.setId(null);
        assertThat(chatBookingDTO1).isNotEqualTo(chatBookingDTO2);
    }
}
