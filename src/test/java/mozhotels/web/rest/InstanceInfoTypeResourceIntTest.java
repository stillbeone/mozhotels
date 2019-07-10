package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceInfoType;
import mozhotels.repository.InstanceInfoTypeRepository;
import mozhotels.repository.search.InstanceInfoTypeSearchRepository;

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

import mozhotels.domain.enumeration.InfoType;

/**
 * Test class for the InstanceInfoTypeResource REST controller.
 *
 * @see InstanceInfoTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceInfoTypeResourceIntTest {


    private static final InfoType DEFAULT_INSTANCE_INFO_TYPE = InfoType.POLICY;
    private static final InfoType UPDATED_INSTANCE_INFO_TYPE = InfoType.USEFUL;
    private static final String DEFAULT_INSTANCE_INFO_TYPE_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_INFO_TYPE_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private InstanceInfoTypeRepository instanceInfoTypeRepository;

    @Inject
    private InstanceInfoTypeSearchRepository instanceInfoTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceInfoTypeMockMvc;

    private InstanceInfoType instanceInfoType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceInfoTypeResource instanceInfoTypeResource = new InstanceInfoTypeResource();
        ReflectionTestUtils.setField(instanceInfoTypeResource, "instanceInfoTypeSearchRepository", instanceInfoTypeSearchRepository);
        ReflectionTestUtils.setField(instanceInfoTypeResource, "instanceInfoTypeRepository", instanceInfoTypeRepository);
        this.restInstanceInfoTypeMockMvc = MockMvcBuilders.standaloneSetup(instanceInfoTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceInfoTypeSearchRepository.deleteAll();
        instanceInfoType = new InstanceInfoType();
        instanceInfoType.setInstanceInfoType(DEFAULT_INSTANCE_INFO_TYPE);
        instanceInfoType.setInstanceInfoTypeName(DEFAULT_INSTANCE_INFO_TYPE_NAME);
        instanceInfoType.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createInstanceInfoType() throws Exception {
        int databaseSizeBeforeCreate = instanceInfoTypeRepository.findAll().size();

        // Create the InstanceInfoType

        restInstanceInfoTypeMockMvc.perform(post("/api/instance-info-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceInfoType)))
                .andExpect(status().isCreated());

        // Validate the InstanceInfoType in the database
        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        assertThat(instanceInfoTypes).hasSize(databaseSizeBeforeCreate + 1);
        InstanceInfoType testInstanceInfoType = instanceInfoTypes.get(instanceInfoTypes.size() - 1);
        assertThat(testInstanceInfoType.getInstanceInfoType()).isEqualTo(DEFAULT_INSTANCE_INFO_TYPE);
        assertThat(testInstanceInfoType.getInstanceInfoTypeName()).isEqualTo(DEFAULT_INSTANCE_INFO_TYPE_NAME);
        assertThat(testInstanceInfoType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the InstanceInfoType in ElasticSearch
        InstanceInfoType instanceInfoTypeEs = instanceInfoTypeSearchRepository.findOne(testInstanceInfoType.getId());
        assertThat(instanceInfoTypeEs).isEqualToComparingFieldByField(testInstanceInfoType);
    }

    @Test
    @Transactional
    public void checkInstanceInfoTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceInfoTypeRepository.findAll().size();
        // set the field null
        instanceInfoType.setInstanceInfoType(null);

        // Create the InstanceInfoType, which fails.

        restInstanceInfoTypeMockMvc.perform(post("/api/instance-info-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceInfoType)))
                .andExpect(status().isBadRequest());

        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        assertThat(instanceInfoTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInstanceInfoTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceInfoTypeRepository.findAll().size();
        // set the field null
        instanceInfoType.setInstanceInfoTypeName(null);

        // Create the InstanceInfoType, which fails.

        restInstanceInfoTypeMockMvc.perform(post("/api/instance-info-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceInfoType)))
                .andExpect(status().isBadRequest());

        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        assertThat(instanceInfoTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceInfoTypes() throws Exception {
        // Initialize the database
        instanceInfoTypeRepository.saveAndFlush(instanceInfoType);

        // Get all the instanceInfoTypes
        restInstanceInfoTypeMockMvc.perform(get("/api/instance-info-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceInfoType.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceInfoType").value(hasItem(DEFAULT_INSTANCE_INFO_TYPE.toString())))
                .andExpect(jsonPath("$.[*].instanceInfoTypeName").value(hasItem(DEFAULT_INSTANCE_INFO_TYPE_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getInstanceInfoType() throws Exception {
        // Initialize the database
        instanceInfoTypeRepository.saveAndFlush(instanceInfoType);

        // Get the instanceInfoType
        restInstanceInfoTypeMockMvc.perform(get("/api/instance-info-types/{id}", instanceInfoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceInfoType.getId().intValue()))
            .andExpect(jsonPath("$.instanceInfoType").value(DEFAULT_INSTANCE_INFO_TYPE.toString()))
            .andExpect(jsonPath("$.instanceInfoTypeName").value(DEFAULT_INSTANCE_INFO_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceInfoType() throws Exception {
        // Get the instanceInfoType
        restInstanceInfoTypeMockMvc.perform(get("/api/instance-info-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceInfoType() throws Exception {
        // Initialize the database
        instanceInfoTypeRepository.saveAndFlush(instanceInfoType);
        instanceInfoTypeSearchRepository.save(instanceInfoType);
        int databaseSizeBeforeUpdate = instanceInfoTypeRepository.findAll().size();

        // Update the instanceInfoType
        InstanceInfoType updatedInstanceInfoType = new InstanceInfoType();
        updatedInstanceInfoType.setId(instanceInfoType.getId());
        updatedInstanceInfoType.setInstanceInfoType(UPDATED_INSTANCE_INFO_TYPE);
        updatedInstanceInfoType.setInstanceInfoTypeName(UPDATED_INSTANCE_INFO_TYPE_NAME);
        updatedInstanceInfoType.setDescription(UPDATED_DESCRIPTION);

        restInstanceInfoTypeMockMvc.perform(put("/api/instance-info-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceInfoType)))
                .andExpect(status().isOk());

        // Validate the InstanceInfoType in the database
        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        assertThat(instanceInfoTypes).hasSize(databaseSizeBeforeUpdate);
        InstanceInfoType testInstanceInfoType = instanceInfoTypes.get(instanceInfoTypes.size() - 1);
        assertThat(testInstanceInfoType.getInstanceInfoType()).isEqualTo(UPDATED_INSTANCE_INFO_TYPE);
        assertThat(testInstanceInfoType.getInstanceInfoTypeName()).isEqualTo(UPDATED_INSTANCE_INFO_TYPE_NAME);
        assertThat(testInstanceInfoType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the InstanceInfoType in ElasticSearch
        InstanceInfoType instanceInfoTypeEs = instanceInfoTypeSearchRepository.findOne(testInstanceInfoType.getId());
        assertThat(instanceInfoTypeEs).isEqualToComparingFieldByField(testInstanceInfoType);
    }

    @Test
    @Transactional
    public void deleteInstanceInfoType() throws Exception {
        // Initialize the database
        instanceInfoTypeRepository.saveAndFlush(instanceInfoType);
        instanceInfoTypeSearchRepository.save(instanceInfoType);
        int databaseSizeBeforeDelete = instanceInfoTypeRepository.findAll().size();

        // Get the instanceInfoType
        restInstanceInfoTypeMockMvc.perform(delete("/api/instance-info-types/{id}", instanceInfoType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceInfoTypeExistsInEs = instanceInfoTypeSearchRepository.exists(instanceInfoType.getId());
        assertThat(instanceInfoTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        assertThat(instanceInfoTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceInfoType() throws Exception {
        // Initialize the database
        instanceInfoTypeRepository.saveAndFlush(instanceInfoType);
        instanceInfoTypeSearchRepository.save(instanceInfoType);

        // Search the instanceInfoType
        restInstanceInfoTypeMockMvc.perform(get("/api/_search/instance-info-types?query=id:" + instanceInfoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceInfoType.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceInfoType").value(hasItem(DEFAULT_INSTANCE_INFO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].instanceInfoTypeName").value(hasItem(DEFAULT_INSTANCE_INFO_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
