package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.LocalTur;
import mozhotels.repository.LocalTurRepository;
import mozhotels.repository.search.LocalTurSearchRepository;

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


/**
 * Test class for the LocalTurResource REST controller.
 *
 * @see LocalTurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocalTurResourceIntTest {

    private static final String DEFAULT_LOCAL_TUR_NAME = "AAAAA";
    private static final String UPDATED_LOCAL_TUR_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final byte[] DEFAULT_PHOTO_PRINCIPAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_PRINCIPAL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/png";

    @Inject
    private LocalTurRepository localTurRepository;

    @Inject
    private LocalTurSearchRepository localTurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocalTurMockMvc;

    private LocalTur localTur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocalTurResource localTurResource = new LocalTurResource();
        ReflectionTestUtils.setField(localTurResource, "localTurSearchRepository", localTurSearchRepository);
        ReflectionTestUtils.setField(localTurResource, "localTurRepository", localTurRepository);
        this.restLocalTurMockMvc = MockMvcBuilders.standaloneSetup(localTurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        localTurSearchRepository.deleteAll();
        localTur = new LocalTur();
        localTur.setLocalTurName(DEFAULT_LOCAL_TUR_NAME);
        localTur.setDescription(DEFAULT_DESCRIPTION);
        localTur.setPhotoPrincipal(DEFAULT_PHOTO_PRINCIPAL);
        localTur.setPhotoPrincipalContentType(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createLocalTur() throws Exception {
        int databaseSizeBeforeCreate = localTurRepository.findAll().size();

        // Create the LocalTur

        restLocalTurMockMvc.perform(post("/api/local-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localTur)))
                .andExpect(status().isCreated());

        // Validate the LocalTur in the database
        List<LocalTur> localTurs = localTurRepository.findAll();
        assertThat(localTurs).hasSize(databaseSizeBeforeCreate + 1);
        LocalTur testLocalTur = localTurs.get(localTurs.size() - 1);
        assertThat(testLocalTur.getLocalTurName()).isEqualTo(DEFAULT_LOCAL_TUR_NAME);
        assertThat(testLocalTur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLocalTur.getPhotoPrincipal()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL);
        assertThat(testLocalTur.getPhotoPrincipalContentType()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the LocalTur in ElasticSearch
        LocalTur localTurEs = localTurSearchRepository.findOne(testLocalTur.getId());
        assertThat(localTurEs).isEqualToComparingFieldByField(testLocalTur);
    }

    @Test
    @Transactional
    public void checkLocalTurNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = localTurRepository.findAll().size();
        // set the field null
        localTur.setLocalTurName(null);

        // Create the LocalTur, which fails.

        restLocalTurMockMvc.perform(post("/api/local-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localTur)))
                .andExpect(status().isBadRequest());

        List<LocalTur> localTurs = localTurRepository.findAll();
        assertThat(localTurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalTurs() throws Exception {
        // Initialize the database
        localTurRepository.saveAndFlush(localTur);

        // Get all the localTurs
        restLocalTurMockMvc.perform(get("/api/local-turs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(localTur.getId().intValue())))
                .andExpect(jsonPath("$.[*].localTurName").value(hasItem(DEFAULT_LOCAL_TUR_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }

    @Test
    @Transactional
    public void getLocalTur() throws Exception {
        // Initialize the database
        localTurRepository.saveAndFlush(localTur);

        // Get the localTur
        restLocalTurMockMvc.perform(get("/api/local-turs/{id}", localTur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(localTur.getId().intValue()))
            .andExpect(jsonPath("$.localTurName").value(DEFAULT_LOCAL_TUR_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.photoPrincipalContentType").value(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoPrincipal").value(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL)));
    }

    @Test
    @Transactional
    public void getNonExistingLocalTur() throws Exception {
        // Get the localTur
        restLocalTurMockMvc.perform(get("/api/local-turs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalTur() throws Exception {
        // Initialize the database
        localTurRepository.saveAndFlush(localTur);
        localTurSearchRepository.save(localTur);
        int databaseSizeBeforeUpdate = localTurRepository.findAll().size();

        // Update the localTur
        LocalTur updatedLocalTur = new LocalTur();
        updatedLocalTur.setId(localTur.getId());
        updatedLocalTur.setLocalTurName(UPDATED_LOCAL_TUR_NAME);
        updatedLocalTur.setDescription(UPDATED_DESCRIPTION);
        updatedLocalTur.setPhotoPrincipal(UPDATED_PHOTO_PRINCIPAL);
        updatedLocalTur.setPhotoPrincipalContentType(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        restLocalTurMockMvc.perform(put("/api/local-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocalTur)))
                .andExpect(status().isOk());

        // Validate the LocalTur in the database
        List<LocalTur> localTurs = localTurRepository.findAll();
        assertThat(localTurs).hasSize(databaseSizeBeforeUpdate);
        LocalTur testLocalTur = localTurs.get(localTurs.size() - 1);
        assertThat(testLocalTur.getLocalTurName()).isEqualTo(UPDATED_LOCAL_TUR_NAME);
        assertThat(testLocalTur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLocalTur.getPhotoPrincipal()).isEqualTo(UPDATED_PHOTO_PRINCIPAL);
        assertThat(testLocalTur.getPhotoPrincipalContentType()).isEqualTo(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);

        // Validate the LocalTur in ElasticSearch
        LocalTur localTurEs = localTurSearchRepository.findOne(testLocalTur.getId());
        assertThat(localTurEs).isEqualToComparingFieldByField(testLocalTur);
    }

    @Test
    @Transactional
    public void deleteLocalTur() throws Exception {
        // Initialize the database
        localTurRepository.saveAndFlush(localTur);
        localTurSearchRepository.save(localTur);
        int databaseSizeBeforeDelete = localTurRepository.findAll().size();

        // Get the localTur
        restLocalTurMockMvc.perform(delete("/api/local-turs/{id}", localTur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean localTurExistsInEs = localTurSearchRepository.exists(localTur.getId());
        assertThat(localTurExistsInEs).isFalse();

        // Validate the database is empty
        List<LocalTur> localTurs = localTurRepository.findAll();
        assertThat(localTurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocalTur() throws Exception {
        // Initialize the database
        localTurRepository.saveAndFlush(localTur);
        localTurSearchRepository.save(localTur);

        // Search the localTur
        restLocalTurMockMvc.perform(get("/api/_search/local-turs?query=id:" + localTur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localTur.getId().intValue())))
            .andExpect(jsonPath("$.[*].localTurName").value(hasItem(DEFAULT_LOCAL_TUR_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))));
    }
}
