package br.com.supera.smartiot.domain;

import static br.com.supera.smartiot.domain.ConfiguracaoAlertaTestSamples.*;
import static br.com.supera.smartiot.domain.SensorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import br.com.supera.smartiot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfiguracaoAlertaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoAlerta.class);
        ConfiguracaoAlerta configuracaoAlerta1 = getConfiguracaoAlertaSample1();
        ConfiguracaoAlerta configuracaoAlerta2 = new ConfiguracaoAlerta();
        assertThat(configuracaoAlerta1).isNotEqualTo(configuracaoAlerta2);

        configuracaoAlerta2.setId(configuracaoAlerta1.getId());
        assertThat(configuracaoAlerta1).isEqualTo(configuracaoAlerta2);

        configuracaoAlerta2 = getConfiguracaoAlertaSample2();
        assertThat(configuracaoAlerta1).isNotEqualTo(configuracaoAlerta2);
    }

    @Test
    void sensorTest() {
        ConfiguracaoAlerta configuracaoAlerta = getConfiguracaoAlertaRandomSampleGenerator();
        Sensor sensorBack = getSensorRandomSampleGenerator();

        configuracaoAlerta.setSensor(sensorBack);
        assertThat(configuracaoAlerta.getSensor()).isEqualTo(sensorBack);

        configuracaoAlerta.sensor(null);
        assertThat(configuracaoAlerta.getSensor()).isNull();
    }
}
