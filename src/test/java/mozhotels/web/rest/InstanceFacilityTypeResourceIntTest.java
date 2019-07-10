package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceFacilityType;
import mozhotels.repository.InstanceFacilityTypeRepository;
import mozhotels.repository.search.InstanceFacilityTypeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mozhotels.domain.enumeration.FacilityType;

/**
 * Test class for the InstanceFacilityTypeResource REST controller.
 *
 * @see InstanceFacilityTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceFacilityTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_FACILITY_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_FACILITY_TYPE_NAME = "BBBBB";

    private static final FacilityType DEFAULT_FACILITY_TYPE = FacilityType.SERVICE;
    private static final FacilityType UPDATED_FACILITY_TYPE = FacilityType.RESOURCE;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_INSTANCE_FACILITY = false;
    private static final Boolean UPDATED_INSTANCE_FACILITY = true;

    private static final Boolean DEFAULT_INSTANCE_ROOM_FACILITY = false;
    private static final Boolean UPDATED_INSTANCE_ROOM_FACILITY = true;

    private static final Boolean DEFAULT_INSTANCE_BOOKING_FACILITY = false;
    private static final Boolean UPDATED_INSTANCE_BOOKING_FACILITY = true;

    @Inject
    private InstanceFacilityTypeRepository instanceFacilityTypeRepository;

    @Inject
    private InstanceFacilityTypeSearchRepository instanceFacilityTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceFacilityTypeMockMvc;

    private InstanceFacilityType instanceFacilityType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceFacilityTypeResource instanceFacilityTypeResource = new InstanceFacilityTypeResource();
        ReflectionTestUtils.setField(instanceFacilityTypeResource, "instanceFacilityTypeSearchRepository", instanceFacilityTypeSearchRepository);
        ReflectionTestUtils.setField(instanceFacilityTypeResource, "instanceFacilityTypeRepository", instanceFacilityTypeRepository);
        this.restInstanceFacilityTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceFacilityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceFacilityTypeSearchRepository.deleteAll();
        instanceFacilityType = new InstanceFacilityType();
        instanceFacilityType.setInstanceFacilityTypeName(DEFAULT_INSTANCE_FACILITY_TYPE_NAME);
        instanceFacilityType.setFacilityType(DEFAULT_FACILITY_TYPE);
        instanceFacilityType.setDescription(DEFAULT_DESCRIPTION);
        instanceFacilityType.setInstanceFacility(DEFAULT_INSTANCE_FACILITY);
        instanceFacilityType.setInstanceRoomFacility(DEFAULT_INSTANCE_ROOM_FACILITY);
        instanceFacilityType.setInstanceBookingFacility(DEFAULT_INSTANCE_BOOKING_FACILITY);
    }

    @Test
    @Transactional
    public void createInstanceFacilityType() throws Exception {
        int databaseSizeBeforeCreate = instanceFacilityTypeRepository.findAll().size();

        // Create the InstanceFacilityType

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isCreated());

        // Validate the InstanceFacilityType in the database
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceFacilityType testInstanceFacilityType = instanceFacilityTypes.get(instanceFacilityTypes.size() - 1);
        assertThat(testInstanceFacilityType.getInstanceFacilityTypeName()).isEqualTo(DEFAULT_INSTANCE_FACILITY_TYPE_NAME);
        assertThat(testInstanceFacilityType.getFacilityType()).isEqualTo(DEFAULT_FACILITY_TYPE);
        assertThat(testInstanceFacilityType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstanceFacilityType.isInstanceFacility()).isEqualTo(DEFAULT_INSTANCE_FACILITY);
        assertThat(testInstanceFacilityType.isInstanceRoomFacility()).isEqualTo(DEFAULT_INSTANCE_ROOM_FACILITY);
        assertThat(testInstanceFacilityType.isInstanceBookingFacility()).isEqualTo(DEFAULT_INSTANCE_BOOKING_FACILITY);

        // Validate the InstanceFacilityType in ElasticSearch
        InstanceFacilityType instanceFacilityTypeEs = instanceFacilityTypeSearchRepository.findOne(testInstanceFacilityType.getId());
        assertThat(instanceFacilityTypeEs).isEqualToComparingFieldByField(testInstanceFacilityType);
    }

    @Test
    @Transactional
    public void checkInstanceFacilityTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityTypeRepository.findAll().size();
        // set the field null
        instanceFacilityType.setInstanceFacilityTypeName(null);

        // Create the InstanceFacilityType, which fails.

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFacilityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceFacilityTypeRepository.findAll().size();
        // set the field null
        instanceFacilityType.setFacilityType(null);

        // Create the InstanceFacilityType, which fails.

        restInstanceFacilityTypeMockMvc.perform(post("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceFacilityType)))
                .andExpect(status().isBadRequest());

        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceFacilityTypes() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);

        // Get all the instanceFacilityTypes
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacilityType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].facilityType").value(hasItem(DEFAULT_FACILITY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].instanceFacility").value(hasItem(DEFAULT_INSTANCE_FACILITY.booleanValue())))
                .andExpect(jsonPath("$.[*].instanceRoomFacility").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY.booleanValue())))
                .andExpect(jsonPath("$.[*].instanceBookingFacility").value(hasItem(DEFAULT_INSTANCE_BOOKING_FACILITY.booleanValue())));
    }

    @Test
    @Transactional
    public void getInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);

        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types/{id}", instanceFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceFacilityType.getId().intValue()))
            .andExpect(jsonPath("$.instanceFacilityTypeName").value(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.facilityType").value(DEFAULT_FACILITY_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.instanceFacility").value(DEFAULT_INSTANCE_FACILITY.booleanValue()))
            .andExpect(jsonPath("$.instanceRoomFacility").value(DEFAULT_INSTANCE_ROOM_FACILITY.booleanValue()))
            .andExpect(jsonPath("$.instanceBookingFacility").value(DEFAULT_INSTANCE_BOOKING_FACILITY.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceFacilityType() throws Exception {
        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/instance-facility-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);
        int databaseSizeBeforeUpdate = instanceFacilityTypeRepository.findAll().size();

        // Update the instanceFacilityType
        InstanceFacilityType updatedInstanceFacilityType = new InstanceFacilityType();
        updatedInstanceFacilityType.setId(instanceFacilityType.getId());
        updatedInstanceFacilityType.setInstanceFacilityTypeName(UPDATED_INSTANCE_FACILITY_TYPE_NAME);
        updatedInstanceFacilityType.setFacilityType(UPDATED_FACILITY_TYPE);
        updatedInstanceFacilityType.setDescription(UPDATED_DESCRIPTION);
        updatedInstanceFacilityType.setInstanceFacility(UPDATED_INSTANCE_FACILITY);
        updatedInstanceFacilityType.setInstanceRoomFacility(UPDATED_INSTANCE_ROOM_FACILITY);
        updatedInstanceFacilityType.setInstanceBookingFacility(UPDATED_INSTANCE_BOOKING_FACILITY);

        restInstanceFacilityTypeMockMvc.perform(put("/api/instance-facility-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceFacilityType)))
                .andExpect(status().isOk());

        // Validate the InstanceFacilityType in the database
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceFacilityType testInstanceFacilityType = instanceFacilityTypes.get(instanceFacilityTypes.size() - 1);
        assertThat(testInstanceFacilityType.getInstanceFacilityTypeName()).isEqualTo(UPDATED_INSTANCE_FACILITY_TYPE_NAME);
        assertThat(testInstanceFacilityType.getFacilityType()).isEqualTo(UPDATED_FACILITY_TYPE);
        assertThat(testInstanceFacilityType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstanceFacilityType.isInstanceFacility()).isEqualTo(UPDATED_INSTANCE_FACILITY);
        assertThat(testInstanceFacilityType.isInstanceRoomFacility()).isEqualTo(UPDATED_INSTANCE_ROOM_FACILITY);
        assertThat(testInstanceFacilityType.isInstanceBookingFacility()).isEqualTo(UPDATED_INSTANCE_BOOKING_FACILITY);

        // Validate the InstanceFacilityType in ElasticSearch
        InstanceFacilityType instanceFacilityTypeEs = instanceFacilityTypeSearchRepository.findOne(testInstanceFacilityType.getId());
        assertThat(instanceFacilityTypeEs).isEqualToComparingFieldByField(testInstanceFacilityType);
    }

    @Test
    @Transactional
    public void deleteInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);
        int databaseSizeBeforeDelete = instanceFacilityTypeRepository.findAll().size();

        // Get the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(delete("/api/instance-facility-types/{id}", instanceFacilityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceFacilityTypeExistsInEs = instanceFacilityTypeSearchRepository.exists(instanceFacilityType.getId());
        assertThat(instanceFacilityTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceFacilityType> instanceFacilityTypes = instanceFacilityTypeRepository.findAll();
        assertThat(instanceFacilityTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceFacilityType() throws Exception {
        // Initialize the database
        instanceFacilityTypeRepository.saveAndFlush(instanceFacilityType);
        instanceFacilityTypeSearchRepository.save(instanceFacilityType);

        // Search the instanceFacilityType
        restInstanceFacilityTypeMockMvc.perform(get("/api/_search/instance-facility-types?query=id:" + instanceFacilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceFacilityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceFacilityTypeName").value(hasItem(DEFAULT_INSTANCE_FACILITY_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].facilityType").value(hasItem(DEFAULT_FACILITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].instanceFacility").value(hasItem(DEFAULT_INSTANCE_FACILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].instanceRoomFacility").value(hasItem(DEFAULT_INSTANCE_ROOM_FACILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].instanceBookingFacility").value(hasItem(DEFAULT_INSTANCE_BOOKING_FACILITY.booleanValue())));
    }
}
