package br.com.supera.smartiot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoLocationDTO.class);
        GeoLocationDTO geoLocationDTO1 = new GeoLocationDTO();
        geoLocationDTO1.setId(1L);
        GeoLocationDTO geoLocationDTO2 = new GeoLocationDTO();
        assertThat(geoLocationDTO1).isNotEqualTo(geoLocationDTO2);
        geoLocationDTO2.setId(geoLocationDTO1.getId());
        assertThat(geoLocationDTO1).isEqualTo(geoLocationDTO2);
        geoLocationDTO2.setId(2L);
        assertThat(geoLocationDTO1).isNotEqualTo(geoLocationDTO2);
        geoLocationDTO1.setId(null);
        assertThat(geoLocationDTO1).isNotEqualTo(geoLocationDTO2);
    }
}
