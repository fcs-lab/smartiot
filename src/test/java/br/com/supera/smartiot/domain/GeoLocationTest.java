package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.GeoLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GeoLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeoLocation.class);
        GeoLocation geoLocation1 = getGeoLocationSample1();
        GeoLocation geoLocation2 = new GeoLocation();
        assertThat(geoLocation1).isNotEqualTo(geoLocation2);

        geoLocation2.setId(geoLocation1.getId());
        assertThat(geoLocation1).isEqualTo(geoLocation2);

        geoLocation2 = getGeoLocationSample2();
        assertThat(geoLocation1).isNotEqualTo(geoLocation2);
    }
}
