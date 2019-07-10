package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceTurType;
import mozhotels.repository.InstanceTurTypeRepository;
import mozhotels.repository.search.InstanceTurTypeSearchRepository;
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
 * REST controller for managing InstanceTurType.
 */
@RestController
@RequestMapping("/api")
public class InstanceTurTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceTurTypeResource.class);
        
    @Inject
    private InstanceTurTypeRepository instanceTurTypeRepository;
    
    @Inject
    private InstanceTurTypeSearchRepository instanceTurTypeSearchRepository;
    
    /**
     * POST  /instance-tur-types : Create a new instanceTurType.
     *
     * @param instanceTurType the instanceTurType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceTurType, or with status 400 (Bad Request) if the instanceTurType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> createInstanceTurType(@Valid @RequestBody InstanceTurType instanceTurType) throws URISyntaxException {
        log.debug("REST request to save InstanceTurType : {}", instanceTurType);
        if (instanceTurType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceTurType", "idexists", "A new instanceTurType cannot already have an ID")).body(null);
        }
        InstanceTurType result = instanceTurTypeRepository.save(instanceTurType);
        instanceTurTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-tur-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceTurType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-tur-types : Updates an existing instanceTurType.
     *
     * @param instanceTurType the instanceTurType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceTurType,
     * or with status 400 (Bad Request) if the instanceTurType is not valid,
     * or with status 500 (Internal Server Error) if the instanceTurType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> updateInstanceTurType(@Valid @RequestBody InstanceTurType instanceTurType) throws URISyntaxException {
        log.debug("REST request to update InstanceTurType : {}", instanceTurType);
        if (instanceTurType.getId() == null) {
            return createInstanceTurType(instanceTurType);
        }
        InstanceTurType result = instanceTurTypeRepository.save(instanceTurType);
        instanceTurTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceTurType", instanceTurType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-tur-types : get all the instanceTurTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceTurTypes in body
     */
    @RequestMapping(value = "/instance-tur-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceTurType> getAllInstanceTurTypes() {
        log.debug("REST request to get all InstanceTurTypes");
        List<InstanceTurType> instanceTurTypes = instanceTurTypeRepository.findAll();
        return instanceTurTypes;
    }

    /**
     * GET  /instance-tur-types/:id : get the "id" instanceTurType.
     *
     * @param id the id of the instanceTurType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceTurType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-tur-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceTurType> getInstanceTurType(@PathVariable Long id) {
        log.debug("REST request to get InstanceTurType : {}", id);
        InstanceTurType instanceTurType = instanceTurTypeRepository.findOne(id);
        return Optional.ofNullable(instanceTurType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-tur-types/:id : delete the "id" instanceTurType.
     *
     * @param id the id of the instanceTurType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-tur-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceTurType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceTurType : {}", id);
        instanceTurTypeRepository.delete(id);
        instanceTurTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceTurType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-tur-types?query=:query : search for the instanceTurType corresponding
     * to the query.
     *
     * @param query the query of the instanceTurType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-tur-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceTurType> searchInstanceTurTypes(@RequestParam String query) {
        log.debug("REST request to search InstanceTurTypes for query {}", query);
        return StreamSupport
            .stream(instanceTurTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
