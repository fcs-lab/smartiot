package br.com.supera.smartiot.web.rest;

import static br.com.supera.smartiot.domain.CarRideAsserts.*;
import static br.com.supera.smartiot.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.supera.smartiot.IntegrationTest;
import br.com.supera.smartiot.domain.CarRide;
import br.com.supera.smartiot.repository.CarRideRepository;
import br.com.supera.smartiot.service.dto.CarRideDTO;
import br.com.supera.smartiot.service.mapper.CarRideMapper;
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
 * Integration tests for the {@link CarRideResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CarRideResourceIT {

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE_SEATS = 1;
    private static final Integer UPDATED_AVAILABLE_SEATS = 2;

    private static final String ENTITY_API_URL = "/api/car-rides";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarRideRepository carRideRepository;

    @Autowired
    private CarRideMapper carRideMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarRideMockMvc;

    private CarRide carRide;

    private CarRide insertedCarRide;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarRide createEntity(EntityManager em) {
        CarRide carRide = new CarRide()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .origin(DEFAULT_ORIGIN)
            .destination(DEFAULT_DESTINATION)
            .availableSeats(DEFAULT_AVAILABLE_SEATS);
        return carRide;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarRide createUpdatedEntity(EntityManager em) {
        CarRide carRide = new CarRide()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .availableSeats(UPDATED_AVAILABLE_SEATS);
        return carRide;
    }

    @BeforeEach
    public void initTest() {
        carRide = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCarRide != null) {
            carRideRepository.delete(insertedCarRide);
            insertedCarRide = null;
        }
    }

    @Test
    @Transactional
    void createCarRide() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);
        var returnedCarRideDTO = om.readValue(
            restCarRideMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarRideDTO.class
        );

        // Validate the CarRide in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCarRide = carRideMapper.toEntity(returnedCarRideDTO);
        assertCarRideUpdatableFieldsEquals(returnedCarRide, getPersistedCarRide(returnedCarRide));

        insertedCarRide = returnedCarRide;
    }

    @Test
    @Transactional
    void createCarRideWithExistingId() throws Exception {
        // Create the CarRide with an existing ID
        carRide.setId(1L);
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carRide.setStartTime(null);

        // Create the CarRide, which fails.
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carRide.setEndTime(null);

        // Create the CarRide, which fails.
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOriginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carRide.setOrigin(null);

        // Create the CarRide, which fails.
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carRide.setDestination(null);

        // Create the CarRide, which fails.
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableSeatsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carRide.setAvailableSeats(null);

        // Create the CarRide, which fails.
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        restCarRideMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarRides() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        // Get all the carRideList
        restCarRideMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carRide.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].availableSeats").value(hasItem(DEFAULT_AVAILABLE_SEATS)));
    }

    @Test
    @Transactional
    void getCarRide() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        // Get the carRide
        restCarRideMockMvc
            .perform(get(ENTITY_API_URL_ID, carRide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carRide.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.availableSeats").value(DEFAULT_AVAILABLE_SEATS));
    }

    @Test
    @Transactional
    void getNonExistingCarRide() throws Exception {
        // Get the carRide
        restCarRideMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarRide() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carRide
        CarRide updatedCarRide = carRideRepository.findById(carRide.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarRide are not directly saved in db
        em.detach(updatedCarRide);
        updatedCarRide
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .availableSeats(UPDATED_AVAILABLE_SEATS);
        CarRideDTO carRideDTO = carRideMapper.toDto(updatedCarRide);

        restCarRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carRideDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarRideToMatchAllProperties(updatedCarRide);
    }

    @Test
    @Transactional
    void putNonExistingCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carRideDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarRideWithPatch() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carRide using partial update
        CarRide partialUpdatedCarRide = new CarRide();
        partialUpdatedCarRide.setId(carRide.getId());

        partialUpdatedCarRide
            .startTime(UPDATED_START_TIME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .availableSeats(UPDATED_AVAILABLE_SEATS);

        restCarRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarRide))
            )
            .andExpect(status().isOk());

        // Validate the CarRide in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarRideUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCarRide, carRide), getPersistedCarRide(carRide));
    }

    @Test
    @Transactional
    void fullUpdateCarRideWithPatch() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carRide using partial update
        CarRide partialUpdatedCarRide = new CarRide();
        partialUpdatedCarRide.setId(carRide.getId());

        partialUpdatedCarRide
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .availableSeats(UPDATED_AVAILABLE_SEATS);

        restCarRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarRide.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarRide))
            )
            .andExpect(status().isOk());

        // Validate the CarRide in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarRideUpdatableFieldsEquals(partialUpdatedCarRide, getPersistedCarRide(partialUpdatedCarRide));
    }

    @Test
    @Transactional
    void patchNonExistingCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carRideDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carRideDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarRide() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carRide.setId(longCount.incrementAndGet());

        // Create the CarRide
        CarRideDTO carRideDTO = carRideMapper.toDto(carRide);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarRideMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carRideDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarRide in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarRide() throws Exception {
        // Initialize the database
        insertedCarRide = carRideRepository.saveAndFlush(carRide);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carRide
        restCarRideMockMvc
            .perform(delete(ENTITY_API_URL_ID, carRide.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carRideRepository.count();
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

    protected CarRide getPersistedCarRide(CarRide carRide) {
        return carRideRepository.findById(carRide.getId()).orElseThrow();
    }

    protected void assertPersistedCarRideToMatchAllProperties(CarRide expectedCarRide) {
        assertCarRideAllPropertiesEquals(expectedCarRide, getPersistedCarRide(expectedCarRide));
    }

    protected void assertPersistedCarRideToMatchUpdatableProperties(CarRide expectedCarRide) {
        assertCarRideAllUpdatablePropertiesEquals(expectedCarRide, getPersistedCarRide(expectedCarRide));
    }
}
