package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.AppDeviceAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.AppDevice;
import br.com.supera.smartiot.domain.enumeration.DeviceType;
import br.com.supera.smartiot.repository.AppDeviceRepository;
import br.com.supera.smartiot.service.dto.AppDeviceDTO;
import br.com.supera.smartiot.service.mapper.AppDeviceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppDeviceResourceIT {

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.GPS;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.TELEMETRY;

    private static final String ENTITY_API_URL = "/api/app-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppDeviceRepository appDeviceRepository;

    @Autowired
    private AppDeviceMapper appDeviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppDeviceMockMvc;

    private AppDevice appDevice;

    private AppDevice insertedAppDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppDevice createEntity(EntityManager em) {
        AppDevice appDevice = new AppDevice().deviceId(DEFAULT_DEVICE_ID).deviceType(DEFAULT_DEVICE_TYPE);
        return appDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppDevice createUpdatedEntity(EntityManager em) {
        AppDevice appDevice = new AppDevice().deviceId(UPDATED_DEVICE_ID).deviceType(UPDATED_DEVICE_TYPE);
        return appDevice;
    }

    @BeforeEach
    public void initTest() {
        appDevice = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAppDevice != null) {
            appDeviceRepository.delete(insertedAppDevice);
            insertedAppDevice = null;
        }
    }

    @Test
    @Transactional
    void createAppDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);
        var returnedAppDeviceDTO = om.readValue(
            restAppDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appDeviceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppDeviceDTO.class
        );

        // Validate the AppDevice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppDevice = appDeviceMapper.toEntity(returnedAppDeviceDTO);
        assertAppDeviceUpdatableFieldsEquals(returnedAppDevice, getPersistedAppDevice(returnedAppDevice));

        insertedAppDevice = returnedAppDevice;
    }

    @Test
    @Transactional
    void createAppDeviceWithExistingId() throws Exception {
        // Create the AppDevice with an existing ID
        appDevice.setId(1L);
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeviceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appDevice.setDeviceId(null);

        // Create the AppDevice, which fails.
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        restAppDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        appDevice.setDeviceType(null);

        // Create the AppDevice, which fails.
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        restAppDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAppDevices() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        // Get all the appDeviceList
        restAppDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getAppDevice() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        // Get the appDevice
        restAppDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, appDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appDevice.getId().intValue()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAppDevice() throws Exception {
        // Get the appDevice
        restAppDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppDevice() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appDevice
        AppDevice updatedAppDevice = appDeviceRepository.findById(appDevice.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppDevice are not directly saved in db
        em.detach(updatedAppDevice);
        updatedAppDevice.deviceId(UPDATED_DEVICE_ID).deviceType(UPDATED_DEVICE_TYPE);
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(updatedAppDevice);

        restAppDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppDeviceToMatchAllProperties(updatedAppDevice);
    }

    @Test
    @Transactional
    void putNonExistingAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appDevice using partial update
        AppDevice partialUpdatedAppDevice = new AppDevice();
        partialUpdatedAppDevice.setId(appDevice.getId());

        partialUpdatedAppDevice.deviceType(UPDATED_DEVICE_TYPE);

        restAppDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppDevice))
            )
            .andExpect(status().isOk());

        // Validate the AppDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppDeviceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppDevice, appDevice),
            getPersistedAppDevice(appDevice)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appDevice using partial update
        AppDevice partialUpdatedAppDevice = new AppDevice();
        partialUpdatedAppDevice.setId(appDevice.getId());

        partialUpdatedAppDevice.deviceId(UPDATED_DEVICE_ID).deviceType(UPDATED_DEVICE_TYPE);

        restAppDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppDevice))
            )
            .andExpect(status().isOk());

        // Validate the AppDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppDeviceUpdatableFieldsEquals(partialUpdatedAppDevice, getPersistedAppDevice(partialUpdatedAppDevice));
    }

    @Test
    @Transactional
    void patchNonExistingAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appDevice.setId(longCount.incrementAndGet());

        // Create the AppDevice
        AppDeviceDTO appDeviceDTO = appDeviceMapper.toDto(appDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppDevice() throws Exception {
        // Initialize the database
        insertedAppDevice = appDeviceRepository.saveAndFlush(appDevice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appDevice
        restAppDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, appDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appDeviceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AppDevice getPersistedAppDevice(AppDevice appDevice) {
        return appDeviceRepository.findById(appDevice.getId()).orElseThrow();
    }

    protected void assertPersistedAppDeviceToMatchAllProperties(AppDevice expectedAppDevice) {
        assertAppDeviceAllPropertiesEquals(expectedAppDevice, getPersistedAppDevice(expectedAppDevice));
    }

    protected void assertPersistedAppDeviceToMatchUpdatableProperties(AppDevice expectedAppDevice) {
        assertAppDeviceAllUpdatablePropertiesEquals(expectedAppDevice, getPersistedAppDevice(expectedAppDevice));
    }
}
