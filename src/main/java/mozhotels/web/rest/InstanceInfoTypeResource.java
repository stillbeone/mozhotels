package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceInfoType;
import mozhotels.repository.InstanceInfoTypeRepository;
import mozhotels.repository.search.InstanceInfoTypeSearchRepository;
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
 * REST controller for managing InstanceInfoType.
 */
@RestController
@RequestMapping("/api")
public class InstanceInfoTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceInfoTypeResource.class);
        
    @Inject
    private InstanceInfoTypeRepository instanceInfoTypeRepository;
    
    @Inject
    private InstanceInfoTypeSearchRepository instanceInfoTypeSearchRepository;
    
    /**
     * POST  /instance-info-types : Create a new instanceInfoType.
     *
     * @param instanceInfoType the instanceInfoType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceInfoType, or with status 400 (Bad Request) if the instanceInfoType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-info-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfoType> createInstanceInfoType(@Valid @RequestBody InstanceInfoType instanceInfoType) throws URISyntaxException {
        log.debug("REST request to save InstanceInfoType : {}", instanceInfoType);
        if (instanceInfoType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceInfoType", "idexists", "A new instanceInfoType cannot already have an ID")).body(null);
        }
        InstanceInfoType result = instanceInfoTypeRepository.save(instanceInfoType);
        instanceInfoTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-info-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceInfoType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-info-types : Updates an existing instanceInfoType.
     *
     * @param instanceInfoType the instanceInfoType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceInfoType,
     * or with status 400 (Bad Request) if the instanceInfoType is not valid,
     * or with status 500 (Internal Server Error) if the instanceInfoType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-info-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfoType> updateInstanceInfoType(@Valid @RequestBody InstanceInfoType instanceInfoType) throws URISyntaxException {
        log.debug("REST request to update InstanceInfoType : {}", instanceInfoType);
        if (instanceInfoType.getId() == null) {
            return createInstanceInfoType(instanceInfoType);
        }
        InstanceInfoType result = instanceInfoTypeRepository.save(instanceInfoType);
        instanceInfoTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceInfoType", instanceInfoType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-info-types : get all the instanceInfoTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceInfoTypes in body
     */
    @RequestMapping(value = "/instance-info-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceInfoType> getAllInstanceInfoTypes() {
        log.debug("REST request to get all InstanceInfoTypes");
        List<InstanceInfoType> instanceInfoTypes = instanceInfoTypeRepository.findAll();
        return instanceInfoTypes;
    }

    /**
     * GET  /instance-info-types/:id : get the "id" instanceInfoType.
     *
     * @param id the id of the instanceInfoType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceInfoType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-info-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfoType> getInstanceInfoType(@PathVariable Long id) {
        log.debug("REST request to get InstanceInfoType : {}", id);
        InstanceInfoType instanceInfoType = instanceInfoTypeRepository.findOne(id);
        return Optional.ofNullable(instanceInfoType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-info-types/:id : delete the "id" instanceInfoType.
     *
     * @param id the id of the instanceInfoType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-info-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceInfoType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceInfoType : {}", id);
        instanceInfoTypeRepository.delete(id);
        instanceInfoTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceInfoType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-info-types?query=:query : search for the instanceInfoType corresponding
     * to the query.
     *
     * @param query the query of the instanceInfoType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-info-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceInfoType> searchInstanceInfoTypes(@RequestParam String query) {
        log.debug("REST request to search InstanceInfoTypes for query {}", query);
        return StreamSupport
            .stream(instanceInfoTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
