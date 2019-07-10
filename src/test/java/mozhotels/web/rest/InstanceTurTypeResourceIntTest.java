package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceTurType;
import mozhotels.repository.InstanceTurTypeRepository;
import mozhotels.repository.search.InstanceTurTypeSearchRepository;

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
 * Test class for the InstanceTurTypeResource REST controller.
 *
 * @see InstanceTurTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceTurTypeResourceIntTest {

    private static final String DEFAULT_INSTANCE_TUR_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_TUR_TYPE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstanceTurTypeRepository instanceTurTypeRepository;

    @Inject
    private InstanceTurTypeSearchRepository instanceTurTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceTurTypeMockMvc;

    private InstanceTurType instanceTurType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceTurTypeResource instanceTurTypeResource = new InstanceTurTypeResource();
        ReflectionTestUtils.setField(instanceTurTypeResource, "instanceTurTypeSearchRepository", instanceTurTypeSearchRepository);
        ReflectionTestUtils.setField(instanceTurTypeResource, "instanceTurTypeRepository", instanceTurTypeRepository);
        this.restInstanceTurTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceTurTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceTurTypeSearchRepository.deleteAll();
        instanceTurType = new InstanceTurType();
        instanceTurType.setInstanceTurTypeName(DEFAULT_INSTANCE_TUR_TYPE_NAME);
        instanceTurType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstanceTurType() throws Exception {
        int databaseSizeBeforeCreate = instanceTurTypeRepository.findAll().size();

        // Create the InstanceTurType

        restInstanceTurTypeMockMvc.perform(post("/api/instance-tur-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTurType)))
                .andExpect(status().isCreated());

        // Validate the InstanceTurType in the database
        List<InstanceTurType> instanceTurTypes = instanceTurTypeRepository.findAll();
        assertThat(instanceTurTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceTurType testInstanceTurType = instanceTurTypes.get(instanceTurTypes.size() - 1);
        assertThat(testInstanceTurType.getInstanceTurTypeName()).isEqualTo(DEFAULT_INSTANCE_TUR_TYPE_NAME);
        assertThat(testInstanceTurType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstanceTurType in ElasticSearch
        InstanceTurType instanceTurTypeEs = instanceTurTypeSearchRepository.findOne(testInstanceTurType.getId());
        assertThat(instanceTurTypeEs).isEqualToComparingFieldByField(testInstanceTurType);
    }

    @Test
    @Transactional
    public void checkInstanceTurTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceTurTypeRepository.findAll().size();
        // set the field null
        instanceTurType.setInstanceTurTypeName(null);

        // Create the InstanceTurType, which fails.

        restInstanceTurTypeMockMvc.perform(post("/api/instance-tur-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTurType)))
                .andExpect(status().isBadRequest());

        List<InstanceTurType> instanceTurTypes = instanceTurTypeRepository.findAll();
        assertThat(instanceTurTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceTurTypes() throws Exception {
        // Initialize the database
        instanceTurTypeRepository.saveAndFlush(instanceTurType);

        // Get all the instanceTurTypes
        restInstanceTurTypeMockMvc.perform(get("/api/instance-tur-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceTurType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceTurTypeName").value(hasItem(DEFAULT_INSTANCE_TUR_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstanceTurType() throws Exception {
        // Initialize the database
        instanceTurTypeRepository.saveAndFlush(instanceTurType);

        // Get the instanceTurType
        restInstanceTurTypeMockMvc.perform(get("/api/instance-tur-types/{id}", instanceTurType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceTurType.getId().intValue()))
            .andExpect(jsonPath("$.instanceTurTypeName").value(DEFAULT_INSTANCE_TUR_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceTurType() throws Exception {
        // Get the instanceTurType
        restInstanceTurTypeMockMvc.perform(get("/api/instance-tur-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceTurType() throws Exception {
        // Initialize the database
        instanceTurTypeRepository.saveAndFlush(instanceTurType);
        instanceTurTypeSearchRepository.save(instanceTurType);
        int databaseSizeBeforeUpdate = instanceTurTypeRepository.findAll().size();

        // Update the instanceTurType
        InstanceTurType updatedInstanceTurType = new InstanceTurType();
        updatedInstanceTurType.setId(instanceTurType.getId());
        updatedInstanceTurType.setInstanceTurTypeName(UPDATED_INSTANCE_TUR_TYPE_NAME);
        updatedInstanceTurType.setDescription(UPDATED_DESCRIPTION);

        restInstanceTurTypeMockMvc.perform(put("/api/instance-tur-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceTurType)))
                .andExpect(status().isOk());

        // Validate the InstanceTurType in the database
        List<InstanceTurType> instanceTurTypes = instanceTurTypeRepository.findAll();
        assertThat(instanceTurTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceTurType testInstanceTurType = instanceTurTypes.get(instanceTurTypes.size() - 1);
        assertThat(testInstanceTurType.getInstanceTurTypeName()).isEqualTo(UPDATED_INSTANCE_TUR_TYPE_NAME);
        assertThat(testInstanceTurType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstanceTurType in ElasticSearch
        InstanceTurType instanceTurTypeEs = instanceTurTypeSearchRepository.findOne(testInstanceTurType.getId());
        assertThat(instanceTurTypeEs).isEqualToComparingFieldByField(testInstanceTurType);
    }

    @Test
    @Transactional
    public void deleteInstanceTurType() throws Exception {
        // Initialize the database
        instanceTurTypeRepository.saveAndFlush(instanceTurType);
        instanceTurTypeSearchRepository.save(instanceTurType);
        int databaseSizeBeforeDelete = instanceTurTypeRepository.findAll().size();

        // Get the instanceTurType
        restInstanceTurTypeMockMvc.perform(delete("/api/instance-tur-types/{id}", instanceTurType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceTurTypeExistsInEs = instanceTurTypeSearchRepository.exists(instanceTurType.getId());
        assertThat(instanceTurTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceTurType> instanceTurTypes = instanceTurTypeRepository.findAll();
        assertThat(instanceTurTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceTurType() throws Exception {
        // Initialize the database
        instanceTurTypeRepository.saveAndFlush(instanceTurType);
        instanceTurTypeSearchRepository.save(instanceTurType);

        // Search the instanceTurType
        restInstanceTurTypeMockMvc.perform(get("/api/_search/instance-tur-types?query=id:" + instanceTurType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceTurType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceTurTypeName").value(hasItem(DEFAULT_INSTANCE_TUR_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
