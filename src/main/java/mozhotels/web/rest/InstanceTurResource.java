package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceTur;
import mozhotels.repository.InstanceTurRepository;
import mozhotels.repository.search.InstanceTurSearchRepository;
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
 * REST controller for managing InstanceTur.
 */
@RestController
@RequestMapping("/api")
public class InstanceTurResource {

    private final Logger log = LoggerFactory.getLogger(InstanceTurResource.class);

    @Inject
    private InstanceTurRepository instanceTurRepository;

    @Inject
    private InstanceTurSearchRepository instanceTurSearchRepository;

    /**
     * POST  /instance-turs : Create a new instanceTur.
     *
     * @param instanceTur the instanceTur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceTur, or with status 400 (Bad Request) if the instanceTur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-turs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTur> createInstanceTur(@Valid @RequestBody InstanceTur instanceTur) throws URISyntaxException {
        log.debug("REST request to save InstanceTur : {}", instanceTur);
        if (instanceTur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceTur", "idexists", "A new instanceTur cannot already have an ID")).body(null);
        }
        InstanceTur result = instanceTurRepository.save(instanceTur);
        instanceTurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-turs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceTur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-turs : Updates an existing instanceTur.
     *
     * @param instanceTur the instanceTur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceTur,
     * or with status 400 (Bad Request) if the instanceTur is not valid,
     * or with status 500 (Internal Server Error) if the instanceTur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-turs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTur> updateInstanceTur(@Valid @RequestBody InstanceTur instanceTur) throws URISyntaxException {
        log.debug("REST request to update InstanceTur : {}", instanceTur);
        if (instanceTur.getId() == null) {
            return createInstanceTur(instanceTur);
        }
        InstanceTur result = instanceTurRepository.save(instanceTur);
        instanceTurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceTur", instanceTur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-turs : get all the instanceTurs.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of instanceTurs in body
     */
    @RequestMapping(value = "/instance-turs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceTur> getAllInstanceTurs(@RequestParam(required = false) String filter) {
        if ("instancecontact-is-null".equals(filter)) {
            log.debug("REST request to get all InstanceTurs where instanceContact is null");
            return StreamSupport
                .stream(instanceTurRepository.findAll().spliterator(), false)
                .filter(instanceTur -> instanceTur.getInstanceContact() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all InstanceTurs");
        List<InstanceTur> instanceTurs = instanceTurRepository.findAllWithEagerRelationships();
        return instanceTurs;
    }


    /**
     * GET  /instance-turs/:id : get the "id" instanceTur.
     *
     * @param id the id of the instanceTur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceTur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-turs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTur> getInstanceTur(@PathVariable Long id) {
        log.debug("REST request to get InstanceTur : {}", id);
        InstanceTur instanceTur = instanceTurRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(instanceTur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-turs/:id : delete the "id" instanceTur.
     *
     * @param id the id of the instanceTur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-turs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceTur(@PathVariable Long id) {
        log.debug("REST request to delete InstanceTur : {}", id);
        instanceTurRepository.delete(id);
        instanceTurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceTur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-turs?query=:query : search for the instanceTur corresponding
     * to the query.
     *
     * @param query the query of the instanceTur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-turs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceTur> searchInstanceTurs(@RequestParam String query) {
        log.debug("REST request to search InstanceTurs for query {}", query);
        return StreamSupport
            .stream(instanceTurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
