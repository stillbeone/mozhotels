package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.Tourist;
import mozhotels.repository.TouristRepository;
import mozhotels.repository.search.TouristSearchRepository;
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
 * REST controller for managing Tourist.
 */
@RestController
@RequestMapping("/api")
public class TouristResource {

    private final Logger log = LoggerFactory.getLogger(TouristResource.class);
        
    @Inject
    private TouristRepository touristRepository;
    
    @Inject
    private TouristSearchRepository touristSearchRepository;
    
    /**
     * POST  /tourists : Create a new tourist.
     *
     * @param tourist the tourist to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tourist, or with status 400 (Bad Request) if the tourist has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tourists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tourist> createTourist(@Valid @RequestBody Tourist tourist) throws URISyntaxException {
        log.debug("REST request to save Tourist : {}", tourist);
        if (tourist.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tourist", "idexists", "A new tourist cannot already have an ID")).body(null);
        }
        Tourist result = touristRepository.save(tourist);
        touristSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tourists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tourist", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tourists : Updates an existing tourist.
     *
     * @param tourist the tourist to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tourist,
     * or with status 400 (Bad Request) if the tourist is not valid,
     * or with status 500 (Internal Server Error) if the tourist couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tourists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tourist> updateTourist(@Valid @RequestBody Tourist tourist) throws URISyntaxException {
        log.debug("REST request to update Tourist : {}", tourist);
        if (tourist.getId() == null) {
            return createTourist(tourist);
        }
        Tourist result = touristRepository.save(tourist);
        touristSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tourist", tourist.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tourists : get all the tourists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tourists in body
     */
    @RequestMapping(value = "/tourists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tourist> getAllTourists() {
        log.debug("REST request to get all Tourists");
        List<Tourist> tourists = touristRepository.findAll();
        return tourists;
    }

    /**
     * GET  /tourists/:id : get the "id" tourist.
     *
     * @param id the id of the tourist to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tourist, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tourists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tourist> getTourist(@PathVariable Long id) {
        log.debug("REST request to get Tourist : {}", id);
        Tourist tourist = touristRepository.findOne(id);
        return Optional.ofNullable(tourist)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tourists/:id : delete the "id" tourist.
     *
     * @param id the id of the tourist to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tourists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTourist(@PathVariable Long id) {
        log.debug("REST request to delete Tourist : {}", id);
        touristRepository.delete(id);
        touristSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tourist", id.toString())).build();
    }

    /**
     * SEARCH  /_search/tourists?query=:query : search for the tourist corresponding
     * to the query.
     *
     * @param query the query of the tourist search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/tourists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tourist> searchTourists(@RequestParam String query) {
        log.debug("REST request to search Tourists for query {}", query);
        return StreamSupport
            .stream(touristSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
