package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceActivityType;
import mozhotels.repository.InstanceActivityTypeRepository;
import mozhotels.repository.search.InstanceActivityTypeSearchRepository;

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


/**
 * Test class for the InstanceActivityTypeResource REST controller.
 *
 * @see InstanceActivityTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceActivityTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_ACTIVITY_TYPE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstanceActivityTypeRepository instanceActivityTypeRepository;

    @Inject
    private InstanceActivityTypeSearchRepository instanceActivityTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceActivityTypeMockMvc;

    private InstanceActivityType instanceActivityType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceActivityTypeResource instanceActivityTypeResource = new InstanceActivityTypeResource();
        ReflectionTestUtils.setField(instanceActivityTypeResource, "instanceActivityTypeSearchRepository", instanceActivityTypeSearchRepository);
        ReflectionTestUtils.setField(instanceActivityTypeResource, "instanceActivityTypeRepository", instanceActivityTypeRepository);
        this.restInstanceActivityTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceActivityTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceActivityTypeSearchRepository.deleteAll();
        instanceActivityType = new InstanceActivityType();
        instanceActivityType.setInstanceActivityTypeName(DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME);
        instanceActivityType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstanceActivityType() throws Exception {
        int databaseSizeBeforeCreate = instanceActivityTypeRepository.findAll().size();

        // Create the InstanceActivityType

        restInstanceActivityTypeMockMvc.perform(post("/api/instance-activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceActivityType)))
                .andExpect(status().isCreated());

        // Validate the InstanceActivityType in the database
        List<InstanceActivityType> instanceActivityTypes = instanceActivityTypeRepository.findAll();
        assertThat(instanceActivityTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceActivityType testInstanceActivityType = instanceActivityTypes.get(instanceActivityTypes.size() - 1);
        assertThat(testInstanceActivityType.getInstanceActivityTypeName()).isEqualTo(DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME);
        assertThat(testInstanceActivityType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstanceActivityType in ElasticSearch
        InstanceActivityType instanceActivityTypeEs = instanceActivityTypeSearchRepository.findOne(testInstanceActivityType.getId());
        assertThat(instanceActivityTypeEs).isEqualToComparingFieldByField(testInstanceActivityType);
    }

    @Test
    @Transactional
    public void checkInstanceActivityTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceActivityTypeRepository.findAll().size();
        // set the field null
        instanceActivityType.setInstanceActivityTypeName(null);

        // Create the InstanceActivityType, which fails.

        restInstanceActivityTypeMockMvc.perform(post("/api/instance-activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceActivityType)))
                .andExpect(status().isBadRequest());

        List<InstanceActivityType> instanceActivityTypes = instanceActivityTypeRepository.findAll();
        assertThat(instanceActivityTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceActivityTypes() throws Exception {
        // Initialize the database
        instanceActivityTypeRepository.saveAndFlush(instanceActivityType);

        // Get all the instanceActivityTypes
        restInstanceActivityTypeMockMvc.perform(get("/api/instance-activity-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceActivityType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceActivityTypeName").value(hasItem(DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstanceActivityType() throws Exception {
        // Initialize the database
        instanceActivityTypeRepository.saveAndFlush(instanceActivityType);

        // Get the instanceActivityType
        restInstanceActivityTypeMockMvc.perform(get("/api/instance-activity-types/{id}", instanceActivityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceActivityType.getId().intValue()))
            .andExpect(jsonPath("$.instanceActivityTypeName").value(DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceActivityType() throws Exception {
        // Get the instanceActivityType
        restInstanceActivityTypeMockMvc.perform(get("/api/instance-activity-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceActivityType() throws Exception {
        // Initialize the database
        instanceActivityTypeRepository.saveAndFlush(instanceActivityType);
        instanceActivityTypeSearchRepository.save(instanceActivityType);
        int databaseSizeBeforeUpdate = instanceActivityTypeRepository.findAll().size();

        // Update the instanceActivityType
        InstanceActivityType updatedInstanceActivityType = new InstanceActivityType();
        updatedInstanceActivityType.setId(instanceActivityType.getId());
        updatedInstanceActivityType.setInstanceActivityTypeName(UPDATED_INSTANCE_ACTIVITY_TYPE_NAME);
        updatedInstanceActivityType.setDescription(UPDATED_DESCRIPTION);

        restInstanceActivityTypeMockMvc.perform(put("/api/instance-activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceActivityType)))
                .andExpect(status().isOk());

        // Validate the InstanceActivityType in the database
        List<InstanceActivityType> instanceActivityTypes = instanceActivityTypeRepository.findAll();
        assertThat(instanceActivityTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceActivityType testInstanceActivityType = instanceActivityTypes.get(instanceActivityTypes.size() - 1);
        assertThat(testInstanceActivityType.getInstanceActivityTypeName()).isEqualTo(UPDATED_INSTANCE_ACTIVITY_TYPE_NAME);
        assertThat(testInstanceActivityType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstanceActivityType in ElasticSearch
        InstanceActivityType instanceActivityTypeEs = instanceActivityTypeSearchRepository.findOne(testInstanceActivityType.getId());
        assertThat(instanceActivityTypeEs).isEqualToComparingFieldByField(testInstanceActivityType);
    }

    @Test
    @Transactional
    public void deleteInstanceActivityType() throws Exception {
        // Initialize the database
        instanceActivityTypeRepository.saveAndFlush(instanceActivityType);
        instanceActivityTypeSearchRepository.save(instanceActivityType);
        int databaseSizeBeforeDelete = instanceActivityTypeRepository.findAll().size();

        // Get the instanceActivityType
        restInstanceActivityTypeMockMvc.perform(delete("/api/instance-activity-types/{id}", instanceActivityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceActivityTypeExistsInEs = instanceActivityTypeSearchRepository.exists(instanceActivityType.getId());
        assertThat(instanceActivityTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceActivityType> instanceActivityTypes = instanceActivityTypeRepository.findAll();
        assertThat(instanceActivityTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceActivityType() throws Exception {
        // Initialize the database
        instanceActivityTypeRepository.saveAndFlush(instanceActivityType);
        instanceActivityTypeSearchRepository.save(instanceActivityType);

        // Search the instanceActivityType
        restInstanceActivityTypeMockMvc.perform(get("/api/_search/instance-activity-types?query=id:" + instanceActivityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceActivityType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceActivityTypeName").value(hasItem(DEFAULT_INSTANCE_ACTIVITY_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
