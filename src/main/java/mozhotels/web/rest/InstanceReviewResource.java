package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceReview;
import mozhotels.repository.InstanceReviewRepository;
import mozhotels.repository.search.InstanceReviewSearchRepository;
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
 * REST controller for managing InstanceReview.
 */
@RestController
@RequestMapping("/api")
public class InstanceReviewResource {

    private final Logger log = LoggerFactory.getLogger(InstanceReviewResource.class);
        
    @Inject
    private InstanceReviewRepository instanceReviewRepository;
    
    @Inject
    private InstanceReviewSearchRepository instanceReviewSearchRepository;
    
    /**
     * POST  /instance-reviews : Create a new instanceReview.
     *
     * @param instanceReview the instanceReview to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceReview, or with status 400 (Bad Request) if the instanceReview has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-reviews",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceReview> createInstanceReview(@Valid @RequestBody InstanceReview instanceReview) throws URISyntaxException {
        log.debug("REST request to save InstanceReview : {}", instanceReview);
        if (instanceReview.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceReview", "idexists", "A new instanceReview cannot already have an ID")).body(null);
        }
        InstanceReview result = instanceReviewRepository.save(instanceReview);
        instanceReviewSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceReview", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-reviews : Updates an existing instanceReview.
     *
     * @param instanceReview the instanceReview to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceReview,
     * or with status 400 (Bad Request) if the instanceReview is not valid,
     * or with status 500 (Internal Server Error) if the instanceReview couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-reviews",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceReview> updateInstanceReview(@Valid @RequestBody InstanceReview instanceReview) throws URISyntaxException {
        log.debug("REST request to update InstanceReview : {}", instanceReview);
        if (instanceReview.getId() == null) {
            return createInstanceReview(instanceReview);
        }
        InstanceReview result = instanceReviewRepository.save(instanceReview);
        instanceReviewSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceReview", instanceReview.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-reviews : get all the instanceReviews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceReviews in body
     */
    @RequestMapping(value = "/instance-reviews",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceReview> getAllInstanceReviews() {
        log.debug("REST request to get all InstanceReviews");
        List<InstanceReview> instanceReviews = instanceReviewRepository.findAll();
        return instanceReviews;
    }

    /**
     * GET  /instance-reviews/:id : get the "id" instanceReview.
     *
     * @param id the id of the instanceReview to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceReview, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-reviews/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceReview> getInstanceReview(@PathVariable Long id) {
        log.debug("REST request to get InstanceReview : {}", id);
        InstanceReview instanceReview = instanceReviewRepository.findOne(id);
        return Optional.ofNullable(instanceReview)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-reviews/:id : delete the "id" instanceReview.
     *
     * @param id the id of the instanceReview to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-reviews/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceReview(@PathVariable Long id) {
        log.debug("REST request to delete InstanceReview : {}", id);
        instanceReviewRepository.delete(id);
        instanceReviewSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceReview", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-reviews?query=:query : search for the instanceReview corresponding
     * to the query.
     *
     * @param query the query of the instanceReview search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-reviews",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceReview> searchInstanceReviews(@RequestParam String query) {
        log.debug("REST request to search InstanceReviews for query {}", query);
        return StreamSupport
            .stream(instanceReviewSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
