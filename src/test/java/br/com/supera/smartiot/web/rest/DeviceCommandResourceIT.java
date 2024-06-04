package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.DeviceCommandAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.DeviceCommand;
import br.com.supera.smartiot.domain.enumeration.CommandStatus;
import br.com.supera.smartiot.repository.DeviceCommandRepository;
import br.com.supera.smartiot.service.dto.DeviceCommandDTO;
import br.com.supera.smartiot.service.mapper.DeviceCommandMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DeviceCommandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceCommandResourceIT {

    private static final String DEFAULT_COMMAND_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SENT_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXECUTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXECUTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CommandStatus DEFAULT_COMMAND_STATUS = CommandStatus.PENDING;
    private static final CommandStatus UPDATED_COMMAND_STATUS = CommandStatus.EXECUTED;

    private static final String ENTITY_API_URL = "/api/device-commands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceCommandRepository deviceCommandRepository;

    @Autowired
    private DeviceCommandMapper deviceCommandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceCommandMockMvc;

    private DeviceCommand deviceCommand;

    private DeviceCommand insertedDeviceCommand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceCommand createEntity(EntityManager em) {
        DeviceCommand deviceCommand = new DeviceCommand()
            .commandType(DEFAULT_COMMAND_TYPE)
            .sentAt(DEFAULT_SENT_AT)
            .executedAt(DEFAULT_EXECUTED_AT)
            .commandStatus(DEFAULT_COMMAND_STATUS);
        return deviceCommand;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceCommand createUpdatedEntity(EntityManager em) {
        DeviceCommand deviceCommand = new DeviceCommand()
            .commandType(UPDATED_COMMAND_TYPE)
            .sentAt(UPDATED_SENT_AT)
            .executedAt(UPDATED_EXECUTED_AT)
            .commandStatus(UPDATED_COMMAND_STATUS);
        return deviceCommand;
    }

    @BeforeEach
    public void initTest() {
        deviceCommand = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeviceCommand != null) {
            deviceCommandRepository.delete(insertedDeviceCommand);
            insertedDeviceCommand = null;
        }
    }

    @Test
    @Transactional
    void createDeviceCommand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);
        var returnedDeviceCommandDTO = om.readValue(
            restDeviceCommandMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeviceCommandDTO.class
        );

        // Validate the DeviceCommand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeviceCommand = deviceCommandMapper.toEntity(returnedDeviceCommandDTO);
        assertDeviceCommandUpdatableFieldsEquals(returnedDeviceCommand, getPersistedDeviceCommand(returnedDeviceCommand));

        insertedDeviceCommand = returnedDeviceCommand;
    }

    @Test
    @Transactional
    void createDeviceCommandWithExistingId() throws Exception {
        // Create the DeviceCommand with an existing ID
        deviceCommand.setId(1L);
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommandTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceCommand.setCommandType(null);

        // Create the DeviceCommand, which fails.
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        restDeviceCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceCommand.setSentAt(null);

        // Create the DeviceCommand, which fails.
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        restDeviceCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommandStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceCommand.setCommandStatus(null);

        // Create the DeviceCommand, which fails.
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        restDeviceCommandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceCommands() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        // Get all the deviceCommandList
        restDeviceCommandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceCommand.getId().intValue())))
            .andExpect(jsonPath("$.[*].commandType").value(hasItem(DEFAULT_COMMAND_TYPE)))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(DEFAULT_SENT_AT.toString())))
            .andExpect(jsonPath("$.[*].executedAt").value(hasItem(DEFAULT_EXECUTED_AT.toString())))
            .andExpect(jsonPath("$.[*].commandStatus").value(hasItem(DEFAULT_COMMAND_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDeviceCommand() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        // Get the deviceCommand
        restDeviceCommandMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceCommand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceCommand.getId().intValue()))
            .andExpect(jsonPath("$.commandType").value(DEFAULT_COMMAND_TYPE))
            .andExpect(jsonPath("$.sentAt").value(DEFAULT_SENT_AT.toString()))
            .andExpect(jsonPath("$.executedAt").value(DEFAULT_EXECUTED_AT.toString()))
            .andExpect(jsonPath("$.commandStatus").value(DEFAULT_COMMAND_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeviceCommand() throws Exception {
        // Get the deviceCommand
        restDeviceCommandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceCommand() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceCommand
        DeviceCommand updatedDeviceCommand = deviceCommandRepository.findById(deviceCommand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeviceCommand are not directly saved in db
        em.detach(updatedDeviceCommand);
        updatedDeviceCommand
            .commandType(UPDATED_COMMAND_TYPE)
            .sentAt(UPDATED_SENT_AT)
            .executedAt(UPDATED_EXECUTED_AT)
            .commandStatus(UPDATED_COMMAND_STATUS);
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(updatedDeviceCommand);

        restDeviceCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceCommandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceCommandDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceCommandToMatchAllProperties(updatedDeviceCommand);
    }

    @Test
    @Transactional
    void putNonExistingDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceCommandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceCommandWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceCommand using partial update
        DeviceCommand partialUpdatedDeviceCommand = new DeviceCommand();
        partialUpdatedDeviceCommand.setId(deviceCommand.getId());

        partialUpdatedDeviceCommand.commandType(UPDATED_COMMAND_TYPE).sentAt(UPDATED_SENT_AT).commandStatus(UPDATED_COMMAND_STATUS);

        restDeviceCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceCommand))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCommand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceCommandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeviceCommand, deviceCommand),
            getPersistedDeviceCommand(deviceCommand)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeviceCommandWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceCommand using partial update
        DeviceCommand partialUpdatedDeviceCommand = new DeviceCommand();
        partialUpdatedDeviceCommand.setId(deviceCommand.getId());

        partialUpdatedDeviceCommand
            .commandType(UPDATED_COMMAND_TYPE)
            .sentAt(UPDATED_SENT_AT)
            .executedAt(UPDATED_EXECUTED_AT)
            .commandStatus(UPDATED_COMMAND_STATUS);

        restDeviceCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceCommand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceCommand))
            )
            .andExpect(status().isOk());

        // Validate the DeviceCommand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceCommandUpdatableFieldsEquals(partialUpdatedDeviceCommand, getPersistedDeviceCommand(partialUpdatedDeviceCommand));
    }

    @Test
    @Transactional
    void patchNonExistingDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceCommandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceCommandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceCommand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceCommand.setId(longCount.incrementAndGet());

        // Create the DeviceCommand
        DeviceCommandDTO deviceCommandDTO = deviceCommandMapper.toDto(deviceCommand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceCommandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deviceCommandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceCommand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceCommand() throws Exception {
        // Initialize the database
        insertedDeviceCommand = deviceCommandRepository.saveAndFlush(deviceCommand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deviceCommand
        restDeviceCommandMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceCommand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceCommandRepository.count();
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

    protected DeviceCommand getPersistedDeviceCommand(DeviceCommand deviceCommand) {
        return deviceCommandRepository.findById(deviceCommand.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceCommandToMatchAllProperties(DeviceCommand expectedDeviceCommand) {
        assertDeviceCommandAllPropertiesEquals(expectedDeviceCommand, getPersistedDeviceCommand(expectedDeviceCommand));
    }

    protected void assertPersistedDeviceCommandToMatchUpdatableProperties(DeviceCommand expectedDeviceCommand) {
        assertDeviceCommandAllUpdatablePropertiesEquals(expectedDeviceCommand, getPersistedDeviceCommand(expectedDeviceCommand));
    }
}
