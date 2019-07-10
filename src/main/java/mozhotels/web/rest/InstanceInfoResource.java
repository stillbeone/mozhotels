package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.InstanceInfo;
import mozhotels.repository.InstanceInfoRepository;
import mozhotels.repository.search.InstanceInfoSearchRepository;
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
 * REST controller for managing InstanceInfo.
 */
@RestController
@RequestMapping("/api")
public class InstanceInfoResource {

    private final Logger log = LoggerFactory.getLogger(InstanceInfoResource.class);
        
    @Inject
    private InstanceInfoRepository instanceInfoRepository;
    
    @Inject
    private InstanceInfoSearchRepository instanceInfoSearchRepository;
    
    /**
     * POST  /instance-infos : Create a new instanceInfo.
     *
     * @param instanceInfo the instanceInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new instanceInfo, or with status 400 (Bad Request) if the instanceInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfo> createInstanceInfo(@Valid @RequestBody InstanceInfo instanceInfo) throws URISyntaxException {
        log.debug("REST request to save InstanceInfo : {}", instanceInfo);
        if (instanceInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("instanceInfo", "idexists", "A new instanceInfo cannot already have an ID")).body(null);
        }
        InstanceInfo result = instanceInfoRepository.save(instanceInfo);
        instanceInfoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/instance-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("instanceInfo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /instance-infos : Updates an existing instanceInfo.
     *
     * @param instanceInfo the instanceInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated instanceInfo,
     * or with status 400 (Bad Request) if the instanceInfo is not valid,
     * or with status 500 (Internal Server Error) if the instanceInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/instance-infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfo> updateInstanceInfo(@Valid @RequestBody InstanceInfo instanceInfo) throws URISyntaxException {
        log.debug("REST request to update InstanceInfo : {}", instanceInfo);
        if (instanceInfo.getId() == null) {
            return createInstanceInfo(instanceInfo);
        }
        InstanceInfo result = instanceInfoRepository.save(instanceInfo);
        instanceInfoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("instanceInfo", instanceInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /instance-infos : get all the instanceInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of instanceInfos in body
     */
    @RequestMapping(value = "/instance-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceInfo> getAllInstanceInfos() {
        log.debug("REST request to get all InstanceInfos");
        List<InstanceInfo> instanceInfos = instanceInfoRepository.findAll();
        return instanceInfos;
    }

    /**
     * GET  /instance-infos/:id : get the "id" instanceInfo.
     *
     * @param id the id of the instanceInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the instanceInfo, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/instance-infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstanceInfo> getInstanceInfo(@PathVariable Long id) {
        log.debug("REST request to get InstanceInfo : {}", id);
        InstanceInfo instanceInfo = instanceInfoRepository.findOne(id);
        return Optional.ofNullable(instanceInfo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /instance-infos/:id : delete the "id" instanceInfo.
     *
     * @param id the id of the instanceInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/instance-infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstanceInfo(@PathVariable Long id) {
        log.debug("REST request to delete InstanceInfo : {}", id);
        instanceInfoRepository.delete(id);
        instanceInfoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("instanceInfo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/instance-infos?query=:query : search for the instanceInfo corresponding
     * to the query.
     *
     * @param query the query of the instanceInfo search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/instance-infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InstanceInfo> searchInstanceInfos(@RequestParam String query) {
        log.debug("REST request to search InstanceInfos for query {}", query);
        return StreamSupport
            .stream(instanceInfoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
