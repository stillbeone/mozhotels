package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.Country;
import mozhotels.repository.CountryRepository;
import mozhotels.repository.search.CountrySearchRepository;

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
 * Test class for the CountryResource REST controller.
 *
 * @see CountryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class CountryResourceIntTest {

    private static final String DEFAULT_COUNTRY_NAME = "AAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountrySearchRepository countrySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCountryMockMvc;

    private Country country;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CountryResource countryResource = new CountryResource();
        ReflectionTestUtils.setField(countryResource, "countrySearchRepository", countrySearchRepository);
        ReflectionTestUtils.setField(countryResource, "countryRepository", countryRepository);
        this.restCountryMockMvc = MockMvcBuilders.standaloneSetup(countryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        countrySearchRepository.deleteAll();
        country = new Country();
        country.setCountryName(DEFAULT_COUNTRY_NAME);
        country.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // Create the Country

        restCountryMockMvc.perform(post("/api/countries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(country)))
                .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countries = countryRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countries.get(countries.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
        assertThat(testCountry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Country in ElasticSearch
        Country countryEs = countrySearchRepository.findOne(testCountry.getId());
        assertThat(countryEs).isEqualToComparingFieldByField(testCountry);
    }

    @Test
    @Transactional
    public void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.

        restCountryMockMvc.perform(post("/api/countries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(country)))
                .andExpect(status().isBadRequest());

        List<Country> countries = countryRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countries
        restCountryMockMvc.perform(get("/api/countries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
                .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(country.getId().intValue()))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get("/api/countries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        countrySearchRepository.save(country);
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = new Country();
        updatedCountry.setId(country.getId());
        updatedCountry.setCountryName(UPDATED_COUNTRY_NAME);
        updatedCountry.setDescription(UPDATED_DESCRIPTION);

        restCountryMockMvc.perform(put("/api/countries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCountry)))
                .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countries = countryRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countries.get(countries.size() - 1);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
        assertThat(testCountry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Country in ElasticSearch
        Country countryEs = countrySearchRepository.findOne(testCountry.getId());
        assertThat(countryEs).isEqualToComparingFieldByField(testCountry);
    }

    @Test
    @Transactional
    public void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        countrySearchRepository.save(country);
        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Get the country
        restCountryMockMvc.perform(delete("/api/countries/{id}", country.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean countryExistsInEs = countrySearchRepository.exists(country.getId());
        assertThat(countryExistsInEs).isFalse();

        // Validate the database is empty
        List<Country> countries = countryRepository.findAll();
        assertThat(countries).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);
        countrySearchRepository.save(country);

        // Search the country
        restCountryMockMvc.perform(get("/api/_search/countries?query=id:" + country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(country.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
