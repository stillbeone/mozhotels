package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.GuestTourist;
import mozhotels.repository.GuestTouristRepository;
import mozhotels.repository.search.GuestTouristSearchRepository;

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
 * Test class for the GuestTouristResource REST controller.
 *
 * @see GuestTouristResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class GuestTouristResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";
    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_COUNTRY_RESIDENCE = "AAAAA";
    private static final String UPDATED_COUNTRY_RESIDENCE = "BBBBB";

    @Inject
    private GuestTouristRepository guestTouristRepository;

    @Inject
    private GuestTouristSearchRepository guestTouristSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGuestTouristMockMvc;

    private GuestTourist guestTourist;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GuestTouristResource guestTouristResource = new GuestTouristResource();
        ReflectionTestUtils.setField(guestTouristResource, "guestTouristSearchRepository", guestTouristSearchRepository);
        ReflectionTestUtils.setField(guestTouristResource, "guestTouristRepository", guestTouristRepository);
        this.restGuestTouristMockMvc = MockMvcBuilders.standaloneSetup(guestTouristResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        guestTouristSearchRepository.deleteAll();
        guestTourist = new GuestTourist();
        guestTourist.setFirstName(DEFAULT_FIRST_NAME);
        guestTourist.setLastName(DEFAULT_LAST_NAME);
        guestTourist.setEmail(DEFAULT_EMAIL);
        guestTourist.setCountryResidence(DEFAULT_COUNTRY_RESIDENCE);
    }

    @Test
    @Transactional
    public void createGuestTourist() throws Exception {
        int databaseSizeBeforeCreate = guestTouristRepository.findAll().size();

        // Create the GuestTourist

        restGuestTouristMockMvc.perform(post("/api/guest-tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(guestTourist)))
                .andExpect(status().isCreated());

        // Validate the GuestTourist in the database
        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeCreate + 1);
        GuestTourist testGuestTourist = guestTourists.get(guestTourists.size() - 1);
        assertThat(testGuestTourist.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testGuestTourist.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testGuestTourist.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGuestTourist.getCountryResidence()).isEqualTo(DEFAULT_COUNTRY_RESIDENCE);

        // Validate the GuestTourist in ElasticSearch
        GuestTourist guestTouristEs = guestTouristSearchRepository.findOne(testGuestTourist.getId());
        assertThat(guestTouristEs).isEqualToComparingFieldByField(testGuestTourist);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestTouristRepository.findAll().size();
        // set the field null
        guestTourist.setFirstName(null);

        // Create the GuestTourist, which fails.

        restGuestTouristMockMvc.perform(post("/api/guest-tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(guestTourist)))
                .andExpect(status().isBadRequest());

        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestTouristRepository.findAll().size();
        // set the field null
        guestTourist.setLastName(null);

        // Create the GuestTourist, which fails.

        restGuestTouristMockMvc.perform(post("/api/guest-tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(guestTourist)))
                .andExpect(status().isBadRequest());

        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = guestTouristRepository.findAll().size();
        // set the field null
        guestTourist.setEmail(null);

        // Create the GuestTourist, which fails.

        restGuestTouristMockMvc.perform(post("/api/guest-tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(guestTourist)))
                .andExpect(status().isBadRequest());

        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGuestTourists() throws Exception {
        // Initialize the database
        guestTouristRepository.saveAndFlush(guestTourist);

        // Get all the guestTourists
        restGuestTouristMockMvc.perform(get("/api/guest-tourists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(guestTourist.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].countryResidence").value(hasItem(DEFAULT_COUNTRY_RESIDENCE.toString())));
    }

    @Test
    @Transactional
    public void getGuestTourist() throws Exception {
        // Initialize the database
        guestTouristRepository.saveAndFlush(guestTourist);

        // Get the guestTourist
        restGuestTouristMockMvc.perform(get("/api/guest-tourists/{id}", guestTourist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(guestTourist.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.countryResidence").value(DEFAULT_COUNTRY_RESIDENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGuestTourist() throws Exception {
        // Get the guestTourist
        restGuestTouristMockMvc.perform(get("/api/guest-tourists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGuestTourist() throws Exception {
        // Initialize the database
        guestTouristRepository.saveAndFlush(guestTourist);
        guestTouristSearchRepository.save(guestTourist);
        int databaseSizeBeforeUpdate = guestTouristRepository.findAll().size();

        // Update the guestTourist
        GuestTourist updatedGuestTourist = new GuestTourist();
        updatedGuestTourist.setId(guestTourist.getId());
        updatedGuestTourist.setFirstName(UPDATED_FIRST_NAME);
        updatedGuestTourist.setLastName(UPDATED_LAST_NAME);
        updatedGuestTourist.setEmail(UPDATED_EMAIL);
        updatedGuestTourist.setCountryResidence(UPDATED_COUNTRY_RESIDENCE);

        restGuestTouristMockMvc.perform(put("/api/guest-tourists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGuestTourist)))
                .andExpect(status().isOk());

        // Validate the GuestTourist in the database
        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeUpdate);
        GuestTourist testGuestTourist = guestTourists.get(guestTourists.size() - 1);
        assertThat(testGuestTourist.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testGuestTourist.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testGuestTourist.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGuestTourist.getCountryResidence()).isEqualTo(UPDATED_COUNTRY_RESIDENCE);

        // Validate the GuestTourist in ElasticSearch
        GuestTourist guestTouristEs = guestTouristSearchRepository.findOne(testGuestTourist.getId());
        assertThat(guestTouristEs).isEqualToComparingFieldByField(testGuestTourist);
    }

    @Test
    @Transactional
    public void deleteGuestTourist() throws Exception {
        // Initialize the database
        guestTouristRepository.saveAndFlush(guestTourist);
        guestTouristSearchRepository.save(guestTourist);
        int databaseSizeBeforeDelete = guestTouristRepository.findAll().size();

        // Get the guestTourist
        restGuestTouristMockMvc.perform(delete("/api/guest-tourists/{id}", guestTourist.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean guestTouristExistsInEs = guestTouristSearchRepository.exists(guestTourist.getId());
        assertThat(guestTouristExistsInEs).isFalse();

        // Validate the database is empty
        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        assertThat(guestTourists).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchGuestTourist() throws Exception {
        // Initialize the database
        guestTouristRepository.saveAndFlush(guestTourist);
        guestTouristSearchRepository.save(guestTourist);

        // Search the guestTourist
        restGuestTouristMockMvc.perform(get("/api/_search/guest-tourists?query=id:" + guestTourist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guestTourist.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].countryResidence").value(hasItem(DEFAULT_COUNTRY_RESIDENCE.toString())));
    }
}
