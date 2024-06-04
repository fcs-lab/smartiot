package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CarRideDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarRideDTO.class);
        CarRideDTO carRideDTO1 = new CarRideDTO();
        carRideDTO1.setId(1L);
        CarRideDTO carRideDTO2 = new CarRideDTO();
        assertThat(carRideDTO1).isNotEqualTo(carRideDTO2);
        carRideDTO2.setId(carRideDTO1.getId());
        assertThat(carRideDTO1).isEqualTo(carRideDTO2);
        carRideDTO2.setId(2L);
        assertThat(carRideDTO1).isNotEqualTo(carRideDTO2);
        carRideDTO1.setId(null);
        assertThat(carRideDTO1).isNotEqualTo(carRideDTO2);
    }
}
