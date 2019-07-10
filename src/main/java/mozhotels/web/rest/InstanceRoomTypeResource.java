package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceRoomType;
import mozhotels.repository.InstanceRoomTypeRepository;
import mozhotels.repository.search.InstanceRoomTypeSearchRepository;
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
 * REST controller for managing InstanceRoomType.
 */
@RestController
@RequestMapping("/api")
public class InstanceRoomTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstanceRoomTypeResource.class);
        
    @Inject
    private InstanceRoomTypeRepository instanceRoomTypeRepository;
    
    @Inject
    private InstanceRoomTypeSearchRepository instanceRoomTypeSearchRepository;
    
    /**
     * POST  /instance-room-types : Create a new instanceRoomType.
     *
     * @param instanceRoomType the instanceRoomType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceRoomType, or with status 400 (Bad Request) if the instanceRoomType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomType> createInstanceRoomType(@Valid @RequestBody InstanceRoomType instanceRoomType) throws URISyntaxException {
        log.debug("REST request to save InstanceRoomType : {}", instanceRoomType);
        if (instanceRoomType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceRoomType", "idexists", "A new instanceRoomType cannot already have an ID")).body(null);
        }
        InstanceRoomType result = instanceRoomTypeRepository.save(instanceRoomType);
        instanceRoomTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-room-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceRoomType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-room-types : Updates an existing instanceRoomType.
     *
     * @param instanceRoomType the instanceRoomType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceRoomType,
     * or with status 400 (Bad Request) if the instanceRoomType is not valid,
     * or with status 500 (Internal Server Error) if the instanceRoomType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-room-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomType> updateInstanceRoomType(@Valid @RequestBody InstanceRoomType instanceRoomType) throws URISyntaxException {
        log.debug("REST request to update InstanceRoomType : {}", instanceRoomType);
        if (instanceRoomType.getId() == null) {
            return createInstanceRoomType(instanceRoomType);
        }
        InstanceRoomType result = instanceRoomTypeRepository.save(instanceRoomType);
        instanceRoomTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceRoomType", instanceRoomType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-room-types : get all the instanceRoomTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceRoomTypes in body
     */
    @RequestMapping(value = "/instance-room-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceRoomType> getAllInstanceRoomTypes() {
        log.debug("REST request to get all InstanceRoomTypes");
        List<InstanceRoomType> instanceRoomTypes = instanceRoomTypeRepository.findAllWithEagerRelationships();
        return instanceRoomTypes;
    }

    /**
     * GET  /instance-room-types/:id : get the "id" instanceRoomType.
     *
     * @param id the id of the instanceRoomType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceRoomType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-room-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceRoomType> getInstanceRoomType(@PathVariable Long id) {
        log.debug("REST request to get InstanceRoomType : {}", id);
        InstanceRoomType instanceRoomType = instanceRoomTypeRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(instanceRoomType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-room-types/:id : delete the "id" instanceRoomType.
     *
     * @param id the id of the instanceRoomType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-room-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceRoomType(@PathVariable Long id) {
        log.debug("REST request to delete InstanceRoomType : {}", id);
        instanceRoomTypeRepository.delete(id);
        instanceRoomTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceRoomType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-room-types?query=:query : search for the instanceRoomType corresponding
     * to the query.
     *
     * @param query the query of the instanceRoomType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-room-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceRoomType> searchInstanceRoomTypes(@RequestParam String query) {
        log.debug("REST request to search InstanceRoomTypes for query {}", query);
        return StreamSupport
            .stream(instanceRoomTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
