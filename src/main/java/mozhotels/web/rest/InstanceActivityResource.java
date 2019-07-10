package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceActivity;
import mozhotels.repository.InstanceActivityRepository;
import mozhotels.repository.search.InstanceActivitySearchRepository;
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
 * REST controller for managing InstanceActivity.
 */
@RestController
@RequestMapping("/api")
public class InstanceActivityResource {

    private final Logger log = LoggerFactory.getLogger(InstanceActivityResource.class);
        
    @Inject
    private InstanceActivityRepository instanceActivityRepository;
    
    @Inject
    private InstanceActivitySearchRepository instanceActivitySearchRepository;
    
    /**
     * POST  /instance-activities : Create a new instanceActivity.
     *
     * @param instanceActivity the instanceActivity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceActivity, or with status 400 (Bad Request) if the instanceActivity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-activities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivity> createInstanceActivity(@Valid @RequestBody InstanceActivity instanceActivity) throws URISyntaxException {
        log.debug("REST request to save InstanceActivity : {}", instanceActivity);
        if (instanceActivity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceActivity", "idexists", "A new instanceActivity cannot already have an ID")).body(null);
        }
        InstanceActivity result = instanceActivityRepository.save(instanceActivity);
        instanceActivitySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceActivity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-activities : Updates an existing instanceActivity.
     *
     * @param instanceActivity the instanceActivity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceActivity,
     * or with status 400 (Bad Request) if the instanceActivity is not valid,
     * or with status 500 (Internal Server Error) if the instanceActivity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-activities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivity> updateInstanceActivity(@Valid @RequestBody InstanceActivity instanceActivity) throws URISyntaxException {
        log.debug("REST request to update InstanceActivity : {}", instanceActivity);
        if (instanceActivity.getId() == null) {
            return createInstanceActivity(instanceActivity);
        }
        InstanceActivity result = instanceActivityRepository.save(instanceActivity);
        instanceActivitySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceActivity", instanceActivity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-activities : get all the instanceActivities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceActivities in body
     */
    @RequestMapping(value = "/instance-activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceActivity> getAllInstanceActivities() {
        log.debug("REST request to get all InstanceActivities");
        List<InstanceActivity> instanceActivities = instanceActivityRepository.findAll();
        return instanceActivities;
    }

    /**
     * GET  /instance-activities/:id : get the "id" instanceActivity.
     *
     * @param id the id of the instanceActivity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceActivity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-activities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceActivity> getInstanceActivity(@PathVariable Long id) {
        log.debug("REST request to get InstanceActivity : {}", id);
        InstanceActivity instanceActivity = instanceActivityRepository.findOne(id);
        return Optional.ofNullable(instanceActivity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-activities/:id : delete the "id" instanceActivity.
     *
     * @param id the id of the instanceActivity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-activities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceActivity(@PathVariable Long id) {
        log.debug("REST request to delete InstanceActivity : {}", id);
        instanceActivityRepository.delete(id);
        instanceActivitySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceActivity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-activities?query=:query : search for the instanceActivity corresponding
     * to the query.
     *
     * @param query the query of the instanceActivity search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceActivity> searchInstanceActivities(@RequestParam String query) {
        log.debug("REST request to search InstanceActivities for query {}", query);
        return StreamSupport
            .stream(instanceActivitySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
