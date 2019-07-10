package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.InstanceContact;
import mozhotels.repository.InstanceContactRepository;
import mozhotels.repository.search.InstanceContactSearchRepository;

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
 * Test class for the InstanceContactResource REST controller.
 *
 * @see InstanceContactResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class InstanceContactResourceIntTest {


    private static final Integer DEFAULT_CONTACT_NUMBER_PRINCIPAL = 1;
    private static final Integer UPDATED_CONTACT_NUMBER_PRINCIPAL = 2;
    private static final String DEFAULT_ZIP_CODE = "AAAAA";
    private static final String UPDATED_ZIP_CODE = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_WEBSITE = "AAAAA";
    private static final String UPDATED_WEBSITE = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    @Inject
    private InstanceContactRepository instanceContactRepository;

    @Inject
    private InstanceContactSearchRepository instanceContactSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInstanceContactMockMvc;

    private InstanceContact instanceContact;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstanceContactResource instanceContactResource = new InstanceContactResource();
        ReflectionTestUtils.setField(instanceContactResource, "instanceContactSearchRepository", instanceContactSearchRepository);
        ReflectionTestUtils.setField(instanceContactResource, "instanceContactRepository", instanceContactRepository);
        this.restInstanceContactMockMvc = MockMvcBuilders.standaloneSetup(instanceContactResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        instanceContactSearchRepository.deleteAll();
        instanceContact = new InstanceContact();
        instanceContact.setContactNumberPrincipal(DEFAULT_CONTACT_NUMBER_PRINCIPAL);
        instanceContact.setZipCode(DEFAULT_ZIP_CODE);
        instanceContact.setAddress(DEFAULT_ADDRESS);
        instanceContact.setWebsite(DEFAULT_WEBSITE);
        instanceContact.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createInstanceContact() throws Exception {
        int databaseSizeBeforeCreate = instanceContactRepository.findAll().size();

        // Create the InstanceContact

        restInstanceContactMockMvc.perform(post("/api/instance-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instanceContact)))
                .andExpect(status().isCreated());

        // Validate the InstanceContact in the database
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeCreate + 1);
        InstanceContact testInstanceContact = instanceContacts.get(instanceContacts.size() - 1);
        assertThat(testInstanceContact.getContactNumberPrincipal()).isEqualTo(DEFAULT_CONTACT_NUMBER_PRINCIPAL);
        assertThat(testInstanceContact.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testInstanceContact.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInstanceContact.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testInstanceContact.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the InstanceContact in ElasticSearch
        InstanceContact instanceContactEs = instanceContactSearchRepository.findOne(testInstanceContact.getId());
        assertThat(instanceContactEs).isEqualToComparingFieldByField(testInstanceContact);
    }

    @Test
    @Transactional
    public void getAllInstanceContacts() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);

        // Get all the instanceContacts
        restInstanceContactMockMvc.perform(get("/api/instance-contacts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(instanceContact.getId().intValue())))
                .andExpect(jsonPath("$.[*].contactNumberPrincipal").value(hasItem(DEFAULT_CONTACT_NUMBER_PRINCIPAL)))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);

        // Get the instanceContact
        restInstanceContactMockMvc.perform(get("/api/instance-contacts/{id}", instanceContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(instanceContact.getId().intValue()))
            .andExpect(jsonPath("$.contactNumberPrincipal").value(DEFAULT_CONTACT_NUMBER_PRINCIPAL))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstanceContact() throws Exception {
        // Get the instanceContact
        restInstanceContactMockMvc.perform(get("/api/instance-contacts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);
        int databaseSizeBeforeUpdate = instanceContactRepository.findAll().size();

        // Update the instanceContact
        InstanceContact updatedInstanceContact = new InstanceContact();
        updatedInstanceContact.setId(instanceContact.getId());
        updatedInstanceContact.setContactNumberPrincipal(UPDATED_CONTACT_NUMBER_PRINCIPAL);
        updatedInstanceContact.setZipCode(UPDATED_ZIP_CODE);
        updatedInstanceContact.setAddress(UPDATED_ADDRESS);
        updatedInstanceContact.setWebsite(UPDATED_WEBSITE);
        updatedInstanceContact.setEmail(UPDATED_EMAIL);

        restInstanceContactMockMvc.perform(put("/api/instance-contacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstanceContact)))
                .andExpect(status().isOk());

        // Validate the InstanceContact in the database
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeUpdate);
        InstanceContact testInstanceContact = instanceContacts.get(instanceContacts.size() - 1);
        assertThat(testInstanceContact.getContactNumberPrincipal()).isEqualTo(UPDATED_CONTACT_NUMBER_PRINCIPAL);
        assertThat(testInstanceContact.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testInstanceContact.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInstanceContact.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testInstanceContact.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the InstanceContact in ElasticSearch
        InstanceContact instanceContactEs = instanceContactSearchRepository.findOne(testInstanceContact.getId());
        assertThat(instanceContactEs).isEqualToComparingFieldByField(testInstanceContact);
    }

    @Test
    @Transactional
    public void deleteInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);
        int databaseSizeBeforeDelete = instanceContactRepository.findAll().size();

        // Get the instanceContact
        restInstanceContactMockMvc.perform(delete("/api/instance-contacts/{id}", instanceContact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean instanceContactExistsInEs = instanceContactSearchRepository.exists(instanceContact.getId());
        assertThat(instanceContactExistsInEs).isFalse();

        // Validate the database is empty
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        assertThat(instanceContacts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstanceContact() throws Exception {
        // Initialize the database
        instanceContactRepository.saveAndFlush(instanceContact);
        instanceContactSearchRepository.save(instanceContact);

        // Search the instanceContact
        restInstanceContactMockMvc.perform(get("/api/_search/instance-contacts?query=id:" + instanceContact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instanceContact.getId().intValue())))
            .andExpect(jsonPath("$.[*].contactNumberPrincipal").value(hasItem(DEFAULT_CONTACT_NUMBER_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
}
