package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceActivity;
import mozhotels.repository.InstanceActivityRepository;
import mozhotels.repository.search.InstanceActivitySearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mozhotels.domain.enumeration.ActivityArea;

/**
 * Test class for the InstanceActivityResource REST controller.
 *
 * @see InstanceActivityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceActivityResourceIntTest {

    private static final String DEFAULT_INSTANCE_ACTIVITY_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_ACTIVITY_NAME = "BBBBB";

    private static final ActivityArea DEFAULT_AREA = ActivityArea.INSIDE;
    private static final ActivityArea UPDATED_AREA = ActivityArea.OUTSIDE;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_PHOTO_PRINCIPAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_PRINCIPAL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/png";

    @Inject
    private InstanceActivityRepository instanceActivityRepository;

    @Inject
    private InstanceActivitySearchRepository instanceActivitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceActivityMockMvc;

    private InstanceActivity instanceActivity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceActivityResource instanceActivityResource = new InstanceActivityResource();
        ReflectionTestUtils.setField(instanceActivityResource, "instanceActivitySearchRepository", instanceActivitySearchRepository);
        ReflectionTestUtils.setField(instanceActivityResource, "instanceActivityRepository", instanceActivityRepository);
        this.restInstanceActivityMockMvc = MockMvcBuilders.standaloneSetup(instanceActivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceActivitySearchRepository.deleteAll();
        instanceActivity = new InstanceActivity();
        instanceActivity.setInstanceActivityName(DEFAULT_INSTANCE_ACTIVITY_NAME);
        instanceActivity.setArea(DEFAULT_AREA);
        instanceActivity.setDescription(DEFAULT_DESCRIPTION);
        instanceActivity.setPhotoPrincipal(DEFAULT_PHOTO_PRINCIPAL);
        instanceActivity.setPhotoPrincipalContentType(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createInstanceActivity() throws Exception {
        int databaseSizeBeforeCreate = instanceActivityRepository.findAll().size();

        // Create the InstanceActivity

        restInstanceActivityMockMvc.perform(post("/api/instance-activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceActivity)))
                .andExpect(status().isCreated());

        // Validate the InstanceActivity in the database
        List<InstanceActivity> instanceActivities = instanceActivityRepository.findAll();
        assertThat(instanceActivities).hasSize(databaseSizeBeforeCreate + 1);
        InstanceActivity testInstanceActivity = instanceActivities.get(instanceActivities.size() - 1);
        assertThat(testInstanceActivity.getInstanceActivityName()).isEqualTo(DEFAULT_INSTANCE_ACTIVITY_NAME);
        assertThat(testInstanceActivity.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testInstanceActivity.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstanceActivity.getPhotoPrincipal()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL);
        assertThat(testInstanceActivity.getPhotoPrincipalContentType()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the InstanceActivity in ElasticSearch
        InstanceActivity instanceActivityEs = instanceActivitySearchRepository.findOne(testInstanceActivity.getId());
        assertThat(instanceActivityEs).isEqualToComparingFieldByField(testInstanceActivity);
    }

    @Test
    @Transactional
    public void checkInstanceActivityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceActivityRepository.findAll().size();
        // set the field null
        instanceActivity.setInstanceActivityName(null);

        // Create the InstanceActivity, which fails.

        restInstanceActivityMockMvc.perform(post("/api/instance-activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceActivity)))
                .andExpect(status().isBadRequest());

        List<InstanceActivity> instanceActivities = instanceActivityRepository.findAll();
        assertThat(instanceActivities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceActivities() throws Exception {
        // Initialize the database
        instanceActivityRepository.saveAndFlush(instanceActivity);

        // Get all the instanceActivities
        restInstanceActivityMockMvc.perform(get("/api/instance-activities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceActivity.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceActivityName").value(hasItem(DEFAULT_INSTANCE_ACTIVITY_NAME.toString())))
                .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }

    @Test
    @Transactional
    public void getInstanceActivity() throws Exception {
        // Initialize the database
        instanceActivityRepository.saveAndFlush(instanceActivity);

        // Get the instanceActivity
        restInstanceActivityMockMvc.perform(get("/api/instance-activities/{id}", instanceActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceActivity.getId().intValue()))
            .andExpect(jsonPath("$.instanceActivityName").value(DEFAULT_INSTANCE_ACTIVITY_NAME.toString()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.photoPrincipalContentType").value(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoPrincipal").value(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL)));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceActivity() throws Exception {
        // Get the instanceActivity
        restInstanceActivityMockMvc.perform(get("/api/instance-activities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceActivity() throws Exception {
        // Initialize the database
        instanceActivityRepository.saveAndFlush(instanceActivity);
        instanceActivitySearchRepository.save(instanceActivity);
        int databaseSizeBeforeUpdate = instanceActivityRepository.findAll().size();

        // Update the instanceActivity
        InstanceActivity updatedInstanceActivity = new InstanceActivity();
        updatedInstanceActivity.setId(instanceActivity.getId());
        updatedInstanceActivity.setInstanceActivityName(UPDATED_INSTANCE_ACTIVITY_NAME);
        updatedInstanceActivity.setArea(UPDATED_AREA);
        updatedInstanceActivity.setDescription(UPDATED_DESCRIPTION);
        updatedInstanceActivity.setPhotoPrincipal(UPDATED_PHOTO_PRINCIPAL);
        updatedInstanceActivity.setPhotoPrincipalContentType(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        restInstanceActivityMockMvc.perform(put("/api/instance-activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceActivity)))
                .andExpect(status().isOk());

        // Validate the InstanceActivity in the database
        List<InstanceActivity> instanceActivities = instanceActivityRepository.findAll();
        assertThat(instanceActivities).hasSize(databaseSizeBeforeUpdate);
        InstanceActivity testInstanceActivity = instanceActivities.get(instanceActivities.size() - 1);
        assertThat(testInstanceActivity.getInstanceActivityName()).isEqualTo(UPDATED_INSTANCE_ACTIVITY_NAME);
        assertThat(testInstanceActivity.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testInstanceActivity.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstanceActivity.getPhotoPrincipal()).isEqualTo(UPDATED_PHOTO_PRINCIPAL);
        assertThat(testInstanceActivity.getPhotoPrincipalContentType()).isEqualTo(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the InstanceActivity in ElasticSearch
        InstanceActivity instanceActivityEs = instanceActivitySearchRepository.findOne(testInstanceActivity.getId());
        assertThat(instanceActivityEs).isEqualToComparingFieldByField(testInstanceActivity);
    }

    @Test
    @Transactional
    public void deleteInstanceActivity() throws Exception {
        // Initialize the database
        instanceActivityRepository.saveAndFlush(instanceActivity);
        instanceActivitySearchRepository.save(instanceActivity);
        int databaseSizeBeforeDelete = instanceActivityRepository.findAll().size();

        // Get the instanceActivity
        restInstanceActivityMockMvc.perform(delete("/api/instance-activities/{id}", instanceActivity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceActivityExistsInEs = instanceActivitySearchRepository.exists(instanceActivity.getId());
        assertThat(instanceActivityExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceActivity> instanceActivities = instanceActivityRepository.findAll();
        assertThat(instanceActivities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceActivity() throws Exception {
        // Initialize the database
        instanceActivityRepository.saveAndFlush(instanceActivity);
        instanceActivitySearchRepository.save(instanceActivity);

        // Search the instanceActivity
        restInstanceActivityMockMvc.perform(get("/api/_search/instance-activities?query=id:" + instanceActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceActivityName").value(hasItem(DEFAULT_INSTANCE_ACTIVITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }
}
