package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceTur;
import mozhotels.repository.InstanceTurRepository;
import mozhotels.repository.search.InstanceTurSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mozhotels.domain.enumeration.InstanceRating;
import mozhotels.domain.enumeration.Currency;

/**
 * Test class for the InstanceTurResource REST controller.
 *
 * @see InstanceTurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceTurResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_INSTANCE_TUR_NAME = "AAAAA";
    private static final String UPDATED_INSTANCE_TUR_NAME = "BBBBB";

    private static final InstanceRating DEFAULT_RATING = InstanceRating.NA;
    private static final InstanceRating UPDATED_RATING = InstanceRating.ONE;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Integer DEFAULT_ROOMS = 1;
    private static final Integer UPDATED_ROOMS = 2;

    private static final Integer DEFAULT_BEDS = 1;
    private static final Integer UPDATED_BEDS = 2;

    private static final Integer DEFAULT_FLOORS = 1;
    private static final Integer UPDATED_FLOORS = 2;

    private static final Currency DEFAULT_CURRENCY = Currency.MZN;
    private static final Currency UPDATED_CURRENCY = Currency.USD;

    private static final byte[] DEFAULT_PHOTO_PRINCIPAL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_PRINCIPAL = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE = "image/png";
    private static final String DEFAULT_AGREEMENT_NUMBER = "AAAAA";
    private static final String UPDATED_AGREEMENT_NUMBER = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_EDIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EDIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EDIT_DATE_STR = dateTimeFormatter.format(DEFAULT_EDIT_DATE);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_APPROVAL = false;
    private static final Boolean UPDATED_APPROVAL = true;

    @Inject
    private InstanceTurRepository instanceTurRepository;

    @Inject
    private InstanceTurSearchRepository instanceTurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceTurMockMvc;

    private InstanceTur instanceTur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceTurResource instanceTurResource = new InstanceTurResource();
        ReflectionTestUtils.setField(instanceTurResource, "instanceTurSearchRepository", instanceTurSearchRepository);
        ReflectionTestUtils.setField(instanceTurResource, "instanceTurRepository", instanceTurRepository);
        this.restInstanceTurMockMvc = MockMvcBuilders.standaloneSetup(instanceTurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceTurSearchRepository.deleteAll();
        instanceTur = new InstanceTur();
        instanceTur.setInstanceTurName(DEFAULT_INSTANCE_TUR_NAME);
        instanceTur.setRating(DEFAULT_RATING);
        instanceTur.setDescription(DEFAULT_DESCRIPTION);
        instanceTur.setLatitude(DEFAULT_LATITUDE);
        instanceTur.setLongitude(DEFAULT_LONGITUDE);
        instanceTur.setRooms(DEFAULT_ROOMS);
        instanceTur.setBeds(DEFAULT_BEDS);
        instanceTur.setFloors(DEFAULT_FLOORS);
        instanceTur.setCurrency(DEFAULT_CURRENCY);
        instanceTur.setPhotoPrincipal(DEFAULT_PHOTO_PRINCIPAL);
        instanceTur.setPhotoPrincipalContentType(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
        instanceTur.setAgreementNumber(DEFAULT_AGREEMENT_NUMBER);
        instanceTur.setCreateDate(DEFAULT_CREATE_DATE);
        instanceTur.setEditDate(DEFAULT_EDIT_DATE);
        instanceTur.setActive(DEFAULT_ACTIVE);
        instanceTur.setApproval(DEFAULT_APPROVAL);
    }

    @Test
    @Transactional
    public void createInstanceTur() throws Exception {
        int databaseSizeBeforeCreate = instanceTurRepository.findAll().size();

        // Create the InstanceTur

        restInstanceTurMockMvc.perform(post("/api/instance-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTur)))
                .andExpect(status().isCreated());

        // Validate the InstanceTur in the database
        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeCreate + 1);
        InstanceTur testInstanceTur = instanceTurs.get(instanceTurs.size() - 1);
        assertThat(testInstanceTur.getInstanceTurName()).isEqualTo(DEFAULT_INSTANCE_TUR_NAME);
        assertThat(testInstanceTur.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testInstanceTur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInstanceTur.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testInstanceTur.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testInstanceTur.getRooms()).isEqualTo(DEFAULT_ROOMS);
        assertThat(testInstanceTur.getBeds()).isEqualTo(DEFAULT_BEDS);
        assertThat(testInstanceTur.getFloors()).isEqualTo(DEFAULT_FLOORS);
        assertThat(testInstanceTur.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testInstanceTur.getPhotoPrincipal()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL);
        assertThat(testInstanceTur.getPhotoPrincipalContentType()).isEqualTo(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE);
        assertThat(testInstanceTur.getAgreementNumber()).isEqualTo(DEFAULT_AGREEMENT_NUMBER);
        assertThat(testInstanceTur.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testInstanceTur.getEditDate()).isEqualTo(DEFAULT_EDIT_DATE);
        assertThat(testInstanceTur.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testInstanceTur.isApproval()).isEqualTo(DEFAULT_APPROVAL);

        // Validate the InstanceTur in ElasticSearch
        InstanceTur instanceTurEs = instanceTurSearchRepository.findOne(testInstanceTur.getId());
        assertThat(instanceTurEs).isEqualToComparingFieldByField(testInstanceTur);
    }

    @Test
    @Transactional
    public void checkInstanceTurNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceTurRepository.findAll().size();
        // set the field null
        instanceTur.setInstanceTurName(null);

        // Create the InstanceTur, which fails.

        restInstanceTurMockMvc.perform(post("/api/instance-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTur)))
                .andExpect(status().isBadRequest());

        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceTurRepository.findAll().size();
        // set the field null
        instanceTur.setDescription(null);

        // Create the InstanceTur, which fails.

        restInstanceTurMockMvc.perform(post("/api/instance-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTur)))
                .andExpect(status().isBadRequest());

        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = instanceTurRepository.findAll().size();
        // set the field null
        instanceTur.setCurrency(null);

        // Create the InstanceTur, which fails.

        restInstanceTurMockMvc.perform(post("/api/instance-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceTur)))
                .andExpect(status().isBadRequest());

        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstanceTurs() throws Exception {
        // Initialize the database
        instanceTurRepository.saveAndFlush(instanceTur);

        // Get all the instanceTurs
        restInstanceTurMockMvc.perform(get("/api/instance-turs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceTur.getId().intValue())))
                .andExpect(jsonPath("$.[*].instanceTurName").value(hasItem(DEFAULT_INSTANCE_TUR_NAME.toString())))
                .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
                .andExpect(jsonPath("$.[*].beds").value(hasItem(DEFAULT_BEDS)))
                .andExpect(jsonPath("$.[*].floors").value(hasItem(DEFAULT_FLOORS)))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
                .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))))
                .andExpect(jsonPath("$.[*].agreementNumber").value(hasItem(DEFAULT_AGREEMENT_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
                .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())));
    }

    @Test
    @Transactional
    public void getInstanceTur() throws Exception {
        // Initialize the database
        instanceTurRepository.saveAndFlush(instanceTur);

        // Get the instanceTur
        restInstanceTurMockMvc.perform(get("/api/instance-turs/{id}", instanceTur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceTur.getId().intValue()))
            .andExpect(jsonPath("$.instanceTurName").value(DEFAULT_INSTANCE_TUR_NAME.toString()))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.rooms").value(DEFAULT_ROOMS))
            .andExpect(jsonPath("$.beds").value(DEFAULT_BEDS))
            .andExpect(jsonPath("$.floors").value(DEFAULT_FLOORS))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.photoPrincipalContentType").value(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoPrincipal").value(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL)))
            .andExpect(jsonPath("$.agreementNumber").value(DEFAULT_AGREEMENT_NUMBER.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.editDate").value(DEFAULT_EDIT_DATE_STR))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.approval").value(DEFAULT_APPROVAL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceTur() throws Exception {
        // Get the instanceTur
        restInstanceTurMockMvc.perform(get("/api/instance-turs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceTur() throws Exception {
        // Initialize the database
        instanceTurRepository.saveAndFlush(instanceTur);
        instanceTurSearchRepository.save(instanceTur);
        int databaseSizeBeforeUpdate = instanceTurRepository.findAll().size();

        // Update the instanceTur
        InstanceTur updatedInstanceTur = new InstanceTur();
        updatedInstanceTur.setId(instanceTur.getId());
        updatedInstanceTur.setInstanceTurName(UPDATED_INSTANCE_TUR_NAME);
        updatedInstanceTur.setRating(UPDATED_RATING);
        updatedInstanceTur.setDescription(UPDATED_DESCRIPTION);
        updatedInstanceTur.setLatitude(UPDATED_LATITUDE);
        updatedInstanceTur.setLongitude(UPDATED_LONGITUDE);
        updatedInstanceTur.setRooms(UPDATED_ROOMS);
        updatedInstanceTur.setBeds(UPDATED_BEDS);
        updatedInstanceTur.setFloors(UPDATED_FLOORS);
        updatedInstanceTur.setCurrency(UPDATED_CURRENCY);
        updatedInstanceTur.setPhotoPrincipal(UPDATED_PHOTO_PRINCIPAL);
        updatedInstanceTur.setPhotoPrincipalContentType(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);
        updatedInstanceTur.setAgreementNumber(UPDATED_AGREEMENT_NUMBER);
        updatedInstanceTur.setCreateDate(UPDATED_CREATE_DATE);
        updatedInstanceTur.setEditDate(UPDATED_EDIT_DATE);
        updatedInstanceTur.setActive(UPDATED_ACTIVE);
        updatedInstanceTur.setApproval(UPDATED_APPROVAL);

        restInstanceTurMockMvc.perform(put("/api/instance-turs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceTur)))
                .andExpect(status().isOk());

        // Validate the InstanceTur in the database
        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeUpdate);
        InstanceTur testInstanceTur = instanceTurs.get(instanceTurs.size() - 1);
        assertThat(testInstanceTur.getInstanceTurName()).isEqualTo(UPDATED_INSTANCE_TUR_NAME);
        assertThat(testInstanceTur.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testInstanceTur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInstanceTur.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInstanceTur.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInstanceTur.getRooms()).isEqualTo(UPDATED_ROOMS);
        assertThat(testInstanceTur.getBeds()).isEqualTo(UPDATED_BEDS);
        assertThat(testInstanceTur.getFloors()).isEqualTo(UPDATED_FLOORS);
        assertThat(testInstanceTur.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testInstanceTur.getPhotoPrincipal()).isEqualTo(UPDATED_PHOTO_PRINCIPAL);
        assertThat(testInstanceTur.getPhotoPrincipalContentType()).isEqualTo(UPDATED_PHOTO_PRINCIPAL_CONTENT_TYPE);
        assertThat(testInstanceTur.getAgreementNumber()).isEqualTo(UPDATED_AGREEMENT_NUMBER);
        assertThat(testInstanceTur.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInstanceTur.getEditDate()).isEqualTo(UPDATED_EDIT_DATE);
        assertThat(testInstanceTur.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testInstanceTur.isApproval()).isEqualTo(UPDATED_APPROVAL);

        // Validate the InstanceTur in ElasticSearch
        InstanceTur instanceTurEs = instanceTurSearchRepository.findOne(testInstanceTur.getId());
        assertThat(instanceTurEs).isEqualToComparingFieldByField(testInstanceTur);
    }

    @Test
    @Transactional
    public void deleteInstanceTur() throws Exception {
        // Initialize the database
        instanceTurRepository.saveAndFlush(instanceTur);
        instanceTurSearchRepository.save(instanceTur);
        int databaseSizeBeforeDelete = instanceTurRepository.findAll().size();

        // Get the instanceTur
        restInstanceTurMockMvc.perform(delete("/api/instance-turs/{id}", instanceTur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceTurExistsInEs = instanceTurSearchRepository.exists(instanceTur.getId());
        assertThat(instanceTurExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceTur> instanceTurs = instanceTurRepository.findAll();
        assertThat(instanceTurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceTur() throws Exception {
        // Initialize the database
        instanceTurRepository.saveAndFlush(instanceTur);
        instanceTurSearchRepository.save(instanceTur);

        // Search the instanceTur
        restInstanceTurMockMvc.perform(get("/api/_search/instance-turs?query=id:" + instanceTur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceTur.getId().intValue())))
            .andExpect(jsonPath("$.[*].instanceTurName").value(hasItem(DEFAULT_INSTANCE_TUR_NAME.toString())))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].rooms").value(hasItem(DEFAULT_ROOMS)))
            .andExpect(jsonPath("$.[*].beds").value(hasItem(DEFAULT_BEDS)))
            .andExpect(jsonPath("$.[*].floors").value(hasItem(DEFAULT_FLOORS)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].photoPrincipalContentType").value(hasItem(DEFAULT_PHOTO_PRINCIPAL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoPrincipal").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_PRINCIPAL))))
            .andExpect(jsonPath("$.[*].agreementNumber").value(hasItem(DEFAULT_AGREEMENT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())));
    }
}
