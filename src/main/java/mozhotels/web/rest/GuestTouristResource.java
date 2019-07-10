package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.GuestTourist;
import mozhotels.repository.GuestTouristRepository;
import mozhotels.repository.search.GuestTouristSearchRepository;
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
 * REST controller for managing GuestTourist.
 */
@RestController
@RequestMapping("/api")
public class GuestTouristResource {

    private final Logger log = LoggerFactory.getLogger(GuestTouristResource.class);
        
    @Inject
    private GuestTouristRepository guestTouristRepository;
    
    @Inject
    private GuestTouristSearchRepository guestTouristSearchRepository;
    
    /**
     * POST  /guest-tourists : Create a new guestTourist.
     *
     * @param guestTourist the guestTourist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new guestTourist, or with status 400 (Bad Request) if the guestTourist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/guest-tourists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GuestTourist> createGuestTourist(@Valid @RequestBody GuestTourist guestTourist) throws URISyntaxException {
        log.debug("REST request to save GuestTourist : {}", guestTourist);
        if (guestTourist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("guestTourist", "idexists", "A new guestTourist cannot already have an ID")).body(null);
        }
        GuestTourist result = guestTouristRepository.save(guestTourist);
        guestTouristSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/guest-tourists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("guestTourist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /guest-tourists : Updates an existing guestTourist.
     *
     * @param guestTourist the guestTourist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated guestTourist,
     * or with status 400 (Bad Request) if the guestTourist is not valid,
     * or with status 500 (Internal Server Error) if the guestTourist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/guest-tourists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GuestTourist> updateGuestTourist(@Valid @RequestBody GuestTourist guestTourist) throws URISyntaxException {
        log.debug("REST request to update GuestTourist : {}", guestTourist);
        if (guestTourist.getId() == null) {
            return createGuestTourist(guestTourist);
        }
        GuestTourist result = guestTouristRepository.save(guestTourist);
        guestTouristSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("guestTourist", guestTourist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /guest-tourists : get all the guestTourists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of guestTourists in body
     */
    @RequestMapping(value = "/guest-tourists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GuestTourist> getAllGuestTourists() {
        log.debug("REST request to get all GuestTourists");
        List<GuestTourist> guestTourists = guestTouristRepository.findAll();
        return guestTourists;
    }

    /**
     * GET  /guest-tourists/:id : get the "id" guestTourist.
     *
     * @param id the id of the guestTourist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the guestTourist, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/guest-tourists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GuestTourist> getGuestTourist(@PathVariable Long id) {
        log.debug("REST request to get GuestTourist : {}", id);
        GuestTourist guestTourist = guestTouristRepository.findOne(id);
        return Optional.ofNullable(guestTourist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /guest-tourists/:id : delete the "id" guestTourist.
     *
     * @param id the id of the guestTourist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/guest-tourists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGuestTourist(@PathVariable Long id) {
        log.debug("REST request to delete GuestTourist : {}", id);
        guestTouristRepository.delete(id);
        guestTouristSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("guestTourist", id.toString())).build();
    }

    /**
     * SEARCH  /_search/guest-tourists?query=:query : search for the guestTourist corresponding
     * to the query.
     *
     * @param query the query of the guestTourist search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/guest-tourists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<GuestTourist> searchGuestTourists(@RequestParam String query) {
        log.debug("REST request to search GuestTourists for query {}", query);
        return StreamSupport
            .stream(guestTouristSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
