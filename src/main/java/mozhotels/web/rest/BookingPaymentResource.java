package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.BookingPayment;
import mozhotels.repository.BookingPaymentRepository;
import mozhotels.repository.search.BookingPaymentSearchRepository;
import mozhotels.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BookingPayment.
 */
@RestController
@RequestMapping("/api")
public class BookingPaymentResource {

    private final Logger log = LoggerFactory.getLogger(BookingPaymentResource.class);
        
    @Inject
    private BookingPaymentRepository bookingPaymentRepository;
    
    @Inject
    private BookingPaymentSearchRepository bookingPaymentSearchRepository;
    
    /**
     * POST  /booking-payments : Create a new bookingPayment.
     *
     * @param bookingPayment the bookingPayment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bookingPayment, or with status 400 (Bad Request) if the bookingPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/booking-payments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingPayment> createBookingPayment(@Valid @RequestBody BookingPayment bookingPayment) throws URISyntaxException {
        log.debug("REST request to save BookingPayment : {}", bookingPayment);
        if (bookingPayment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bookingPayment", "idexists", "A new bookingPayment cannot already have an ID")).body(null);
        }
        BookingPayment result = bookingPaymentRepository.save(bookingPayment);
        bookingPaymentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/booking-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bookingPayment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /booking-payments : Updates an existing bookingPayment.
     *
     * @param bookingPayment the bookingPayment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bookingPayment,
     * or with status 400 (Bad Request) if the bookingPayment is not valid,
     * or with status 500 (Internal Server Error) if the bookingPayment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/booking-payments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingPayment> updateBookingPayment(@Valid @RequestBody BookingPayment bookingPayment) throws URISyntaxException {
        log.debug("REST request to update BookingPayment : {}", bookingPayment);
        if (bookingPayment.getId() == null) {
            return createBookingPayment(bookingPayment);
        }
        BookingPayment result = bookingPaymentRepository.save(bookingPayment);
        bookingPaymentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bookingPayment", bookingPayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /booking-payments : get all the bookingPayments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bookingPayments in body
     */
    @RequestMapping(value = "/booking-payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingPayment> getAllBookingPayments() {
        log.debug("REST request to get all BookingPayments");
        List<BookingPayment> bookingPayments = bookingPaymentRepository.findAll();
        return bookingPayments;
    }

    /**
     * GET  /booking-payments/:id : get the "id" bookingPayment.
     *
     * @param id the id of the bookingPayment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bookingPayment, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/booking-payments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BookingPayment> getBookingPayment(@PathVariable Long id) {
        log.debug("REST request to get BookingPayment : {}", id);
        BookingPayment bookingPayment = bookingPaymentRepository.findOne(id);
        return Optional.ofNullable(bookingPayment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /booking-payments/:id : delete the "id" bookingPayment.
     *
     * @param id the id of the bookingPayment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/booking-payments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBookingPayment(@PathVariable Long id) {
        log.debug("REST request to delete BookingPayment : {}", id);
        bookingPaymentRepository.delete(id);
        bookingPaymentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bookingPayment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/booking-payments?query=:query : search for the bookingPayment corresponding
     * to the query.
     *
     * @param query the query of the bookingPayment search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/booking-payments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BookingPayment> searchBookingPayments(@RequestParam String query) {
        log.debug("REST request to search BookingPayments for query {}", query);
        return StreamSupport
            .stream(bookingPaymentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
