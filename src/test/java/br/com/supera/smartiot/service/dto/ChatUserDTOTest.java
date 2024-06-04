package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChatUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatUserDTO.class);
        ChatUserDTO chatUserDTO1 = new ChatUserDTO();
        chatUserDTO1.setId(1L);
        ChatUserDTO chatUserDTO2 = new ChatUserDTO();
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
        chatUserDTO2.setId(chatUserDTO1.getId());
        assertThat(chatUserDTO1).isEqualTo(chatUserDTO2);
        chatUserDTO2.setId(2L);
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
        chatUserDTO1.setId(null);
        assertThat(chatUserDTO1).isNotEqualTo(chatUserDTO2);
    }
}
