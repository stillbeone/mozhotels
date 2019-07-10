package mozhotels.web.rest;

import mozhotels.MozhotelsbookingApp;
import mozhotels.domain.BookingPayment;
import mozhotels.repository.BookingPaymentRepository;
import mozhotels.repository.search.BookingPaymentSearchRepository;

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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mozhotels.domain.enumeration.PaymentType;
import mozhotels.domain.enumeration.PaymentState;
import mozhotels.domain.enumeration.CardType;

/**
 * Test class for the BookingPaymentResource REST controller.
 *
 * @see BookingPaymentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MozhotelsbookingApp.class)
@WebAppConfiguration
@IntegrationTest
public class BookingPaymentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final PaymentType DEFAULT_TYPE = PaymentType.CREDIT_CARD;
    private static final PaymentType UPDATED_TYPE = PaymentType.DEBIT_CARD;
    private static final String DEFAULT_CURRENCY = "AAAAA";
    private static final String UPDATED_CURRENCY = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final PaymentState DEFAULT_STATE = PaymentState.SUBMITED;
    private static final PaymentState UPDATED_STATE = PaymentState.IN_PROGRESS;
    private static final String DEFAULT_CARD_HOLDER = "AAAAA";
    private static final String UPDATED_CARD_HOLDER = "BBBBB";

    private static final CardType DEFAULT_CARD_TYPE = CardType.VISA;
    private static final CardType UPDATED_CARD_TYPE = CardType.MASTERCARD;

    private static final Integer DEFAULT_CARD_NUMBER = 1;
    private static final Integer UPDATED_CARD_NUMBER = 2;

    private static final LocalDate DEFAULT_CARD_EXPIRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CARD_EXPIRY = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_CARD_CCV = 1;
    private static final Integer UPDATED_CARD_CCV = 2;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATE_DATE);

    private static final ZonedDateTime DEFAULT_EDIT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_EDIT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_EDIT_DATE_STR = dateTimeFormatter.format(DEFAULT_EDIT_DATE);

    private static final Boolean DEFAULT_APPROVAL = false;
    private static final Boolean UPDATED_APPROVAL = true;

    @Inject
    private BookingPaymentRepository bookingPaymentRepository;

    @Inject
    private BookingPaymentSearchRepository bookingPaymentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBookingPaymentMockMvc;

    private BookingPayment bookingPayment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BookingPaymentResource bookingPaymentResource = new BookingPaymentResource();
        ReflectionTestUtils.setField(bookingPaymentResource, "bookingPaymentSearchRepository", bookingPaymentSearchRepository);
        ReflectionTestUtils.setField(bookingPaymentResource, "bookingPaymentRepository", bookingPaymentRepository);
        this.restBookingPaymentMockMvc = MockMvcBuilders.standaloneSetup(bookingPaymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bookingPaymentSearchRepository.deleteAll();
        bookingPayment = new BookingPayment();
        bookingPayment.setType(DEFAULT_TYPE);
        bookingPayment.setCurrency(DEFAULT_CURRENCY);
        bookingPayment.setAmount(DEFAULT_AMOUNT);
        bookingPayment.setState(DEFAULT_STATE);
        bookingPayment.setCardHolder(DEFAULT_CARD_HOLDER);
        bookingPayment.setCardType(DEFAULT_CARD_TYPE);
        bookingPayment.setCardNumber(DEFAULT_CARD_NUMBER);
        bookingPayment.setCardExpiry(DEFAULT_CARD_EXPIRY);
        bookingPayment.setCardCcv(DEFAULT_CARD_CCV);
        bookingPayment.setCreateDate(DEFAULT_CREATE_DATE);
        bookingPayment.setEditDate(DEFAULT_EDIT_DATE);
        bookingPayment.setApproval(DEFAULT_APPROVAL);
    }

    @Test
    @Transactional
    public void createBookingPayment() throws Exception {
        int databaseSizeBeforeCreate = bookingPaymentRepository.findAll().size();

        // Create the BookingPayment

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isCreated());

        // Validate the BookingPayment in the database
        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeCreate + 1);
        BookingPayment testBookingPayment = bookingPayments.get(bookingPayments.size() - 1);
        assertThat(testBookingPayment.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testBookingPayment.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testBookingPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testBookingPayment.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBookingPayment.getCardHolder()).isEqualTo(DEFAULT_CARD_HOLDER);
        assertThat(testBookingPayment.getCardType()).isEqualTo(DEFAULT_CARD_TYPE);
        assertThat(testBookingPayment.getCardNumber()).isEqualTo(DEFAULT_CARD_NUMBER);
        assertThat(testBookingPayment.getCardExpiry()).isEqualTo(DEFAULT_CARD_EXPIRY);
        assertThat(testBookingPayment.getCardCcv()).isEqualTo(DEFAULT_CARD_CCV);
        assertThat(testBookingPayment.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testBookingPayment.getEditDate()).isEqualTo(DEFAULT_EDIT_DATE);
        assertThat(testBookingPayment.isApproval()).isEqualTo(DEFAULT_APPROVAL);

        // Validate the BookingPayment in ElasticSearch
        BookingPayment bookingPaymentEs = bookingPaymentSearchRepository.findOne(testBookingPayment.getId());
        assertThat(bookingPaymentEs).isEqualToComparingFieldByField(testBookingPayment);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setType(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCurrency(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setAmount(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setState(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardHolderIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCardHolder(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCardType(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCardNumber(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardExpiryIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCardExpiry(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCardCcvIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCardCcv(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setCreateDate(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEditDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingPaymentRepository.findAll().size();
        // set the field null
        bookingPayment.setEditDate(null);

        // Create the BookingPayment, which fails.

        restBookingPaymentMockMvc.perform(post("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bookingPayment)))
                .andExpect(status().isBadRequest());

        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookingPayments() throws Exception {
        // Initialize the database
        bookingPaymentRepository.saveAndFlush(bookingPayment);

        // Get all the bookingPayments
        restBookingPaymentMockMvc.perform(get("/api/booking-payments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bookingPayment.getId().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
                .andExpect(jsonPath("$.[*].cardHolder").value(hasItem(DEFAULT_CARD_HOLDER.toString())))
                .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
                .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
                .andExpect(jsonPath("$.[*].cardExpiry").value(hasItem(DEFAULT_CARD_EXPIRY.toString())))
                .andExpect(jsonPath("$.[*].cardCcv").value(hasItem(DEFAULT_CARD_CCV)))
                .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
                .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
                .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())));
    }

    @Test
    @Transactional
    public void getBookingPayment() throws Exception {
        // Initialize the database
        bookingPaymentRepository.saveAndFlush(bookingPayment);

        // Get the bookingPayment
        restBookingPaymentMockMvc.perform(get("/api/booking-payments/{id}", bookingPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bookingPayment.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.cardHolder").value(DEFAULT_CARD_HOLDER.toString()))
            .andExpect(jsonPath("$.cardType").value(DEFAULT_CARD_TYPE.toString()))
            .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
            .andExpect(jsonPath("$.cardExpiry").value(DEFAULT_CARD_EXPIRY.toString()))
            .andExpect(jsonPath("$.cardCcv").value(DEFAULT_CARD_CCV))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE_STR))
            .andExpect(jsonPath("$.editDate").value(DEFAULT_EDIT_DATE_STR))
            .andExpect(jsonPath("$.approval").value(DEFAULT_APPROVAL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBookingPayment() throws Exception {
        // Get the bookingPayment
        restBookingPaymentMockMvc.perform(get("/api/booking-payments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookingPayment() throws Exception {
        // Initialize the database
        bookingPaymentRepository.saveAndFlush(bookingPayment);
        bookingPaymentSearchRepository.save(bookingPayment);
        int databaseSizeBeforeUpdate = bookingPaymentRepository.findAll().size();

        // Update the bookingPayment
        BookingPayment updatedBookingPayment = new BookingPayment();
        updatedBookingPayment.setId(bookingPayment.getId());
        updatedBookingPayment.setType(UPDATED_TYPE);
        updatedBookingPayment.setCurrency(UPDATED_CURRENCY);
        updatedBookingPayment.setAmount(UPDATED_AMOUNT);
        updatedBookingPayment.setState(UPDATED_STATE);
        updatedBookingPayment.setCardHolder(UPDATED_CARD_HOLDER);
        updatedBookingPayment.setCardType(UPDATED_CARD_TYPE);
        updatedBookingPayment.setCardNumber(UPDATED_CARD_NUMBER);
        updatedBookingPayment.setCardExpiry(UPDATED_CARD_EXPIRY);
        updatedBookingPayment.setCardCcv(UPDATED_CARD_CCV);
        updatedBookingPayment.setCreateDate(UPDATED_CREATE_DATE);
        updatedBookingPayment.setEditDate(UPDATED_EDIT_DATE);
        updatedBookingPayment.setApproval(UPDATED_APPROVAL);

        restBookingPaymentMockMvc.perform(put("/api/booking-payments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBookingPayment)))
                .andExpect(status().isOk());

        // Validate the BookingPayment in the database
        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeUpdate);
        BookingPayment testBookingPayment = bookingPayments.get(bookingPayments.size() - 1);
        assertThat(testBookingPayment.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testBookingPayment.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testBookingPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testBookingPayment.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBookingPayment.getCardHolder()).isEqualTo(UPDATED_CARD_HOLDER);
        assertThat(testBookingPayment.getCardType()).isEqualTo(UPDATED_CARD_TYPE);
        assertThat(testBookingPayment.getCardNumber()).isEqualTo(UPDATED_CARD_NUMBER);
        assertThat(testBookingPayment.getCardExpiry()).isEqualTo(UPDATED_CARD_EXPIRY);
        assertThat(testBookingPayment.getCardCcv()).isEqualTo(UPDATED_CARD_CCV);
        assertThat(testBookingPayment.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testBookingPayment.getEditDate()).isEqualTo(UPDATED_EDIT_DATE);
        assertThat(testBookingPayment.isApproval()).isEqualTo(UPDATED_APPROVAL);

        // Validate the BookingPayment in ElasticSearch
        BookingPayment bookingPaymentEs = bookingPaymentSearchRepository.findOne(testBookingPayment.getId());
        assertThat(bookingPaymentEs).isEqualToComparingFieldByField(testBookingPayment);
    }

    @Test
    @Transactional
    public void deleteBookingPayment() throws Exception {
        // Initialize the database
        bookingPaymentRepository.saveAndFlush(bookingPayment);
        bookingPaymentSearchRepository.save(bookingPayment);
        int databaseSizeBeforeDelete = bookingPaymentRepository.findAll().size();

        // Get the bookingPayment
        restBookingPaymentMockMvc.perform(delete("/api/booking-payments/{id}", bookingPayment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bookingPaymentExistsInEs = bookingPaymentSearchRepository.exists(bookingPayment.getId());
        assertThat(bookingPaymentExistsInEs).isFalse();

        // Validate the database is empty
        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        assertThat(bookingPayments).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBookingPayment() throws Exception {
        // Initialize the database
        bookingPaymentRepository.saveAndFlush(bookingPayment);
        bookingPaymentSearchRepository.save(bookingPayment);

        // Search the bookingPayment
        restBookingPaymentMockMvc.perform(get("/api/_search/booking-payments?query=id:" + bookingPayment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookingPayment.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].cardHolder").value(hasItem(DEFAULT_CARD_HOLDER.toString())))
            .andExpect(jsonPath("$.[*].cardType").value(hasItem(DEFAULT_CARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cardNumber").value(hasItem(DEFAULT_CARD_NUMBER)))
            .andExpect(jsonPath("$.[*].cardExpiry").value(hasItem(DEFAULT_CARD_EXPIRY.toString())))
            .andExpect(jsonPath("$.[*].cardCcv").value(hasItem(DEFAULT_CARD_CCV)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE_STR)))
            .andExpect(jsonPath("$.[*].editDate").value(hasItem(DEFAULT_EDIT_DATE_STR)))
            .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.booleanValue())));
    }
}
