package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceContact;
import mozhotels.repository.InstanceContactRepository;
import mozhotels.repository.search.InstanceContactSearchRepository;
import mozhotels.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing InstanceContact.
 */
@RestController
@RequestMapping("/api")
public class InstanceContactResource {

    private final Logger log = LoggerFactory.getLogger(InstanceContactResource.class);
        
    @Inject
    private InstanceContactRepository instanceContactRepository;
    
    @Inject
    private InstanceContactSearchRepository instanceContactSearchRepository;
    
    /**
     * POST  /instance-contacts : Create a new instanceContact.
     *
     * @param instanceContact the instanceContact to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceContact, or with status 400 (Bad Request) if the instanceContact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-contacts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceContact> createInstanceContact(@RequestBody InstanceContact instanceContact) throws URISyntaxException {
        log.debug("REST request to save InstanceContact : {}", instanceContact);
        if (instanceContact.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceContact", "idexists", "A new instanceContact cannot already have an ID")).body(null);
        }
        InstanceContact result = instanceContactRepository.save(instanceContact);
        instanceContactSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-contacts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceContact", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-contacts : Updates an existing instanceContact.
     *
     * @param instanceContact the instanceContact to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceContact,
     * or with status 400 (Bad Request) if the instanceContact is not valid,
     * or with status 500 (Internal Server Error) if the instanceContact couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-contacts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceContact> updateInstanceContact(@RequestBody InstanceContact instanceContact) throws URISyntaxException {
        log.debug("REST request to update InstanceContact : {}", instanceContact);
        if (instanceContact.getId() == null) {
            return createInstanceContact(instanceContact);
        }
        InstanceContact result = instanceContactRepository.save(instanceContact);
        instanceContactSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceContact", instanceContact.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-contacts : get all the instanceContacts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceContacts in body
     */
    @RequestMapping(value = "/instance-contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceContact> getAllInstanceContacts() {
        log.debug("REST request to get all InstanceContacts");
        List<InstanceContact> instanceContacts = instanceContactRepository.findAll();
        return instanceContacts;
    }

    /**
     * GET  /instance-contacts/:id : get the "id" instanceContact.
     *
     * @param id the id of the instanceContact to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceContact, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-contacts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceContact> getInstanceContact(@PathVariable Long id) {
        log.debug("REST request to get InstanceContact : {}", id);
        InstanceContact instanceContact = instanceContactRepository.findOne(id);
        return Optional.ofNullable(instanceContact)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-contacts/:id : delete the "id" instanceContact.
     *
     * @param id the id of the instanceContact to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-contacts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceContact(@PathVariable Long id) {
        log.debug("REST request to delete InstanceContact : {}", id);
        instanceContactRepository.delete(id);
        instanceContactSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceContact", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-contacts?query=:query : search for the instanceContact corresponding
     * to the query.
     *
     * @param query the query of the instanceContact search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-contacts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceContact> searchInstanceContacts(@RequestParam String query) {
        log.debug("REST request to search InstanceContacts for query {}", query);
        return StreamSupport
            .stream(instanceContactSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
