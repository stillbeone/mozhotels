package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.Info;
import mozhotels.repository.InfoRepository;
import mozhotels.repository.search.InfoSearchRepository;
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
 * REST controller for managing Info.
 */
@RestController
@RequestMapping("/api")
public class InfoResource {

    private final Logger log = LoggerFactory.getLogger(InfoResource.class);
        
    @Inject
    private InfoRepository infoRepository;
    
    @Inject
    private InfoSearchRepository infoSearchRepository;
    
    /**
     * POST  /infos : Create a new info.
     *
     * @param info the info to create
     * @return the ResponseEntity with status 201 (Created) and with body the new info, or with status 400 (Bad Request) if the info has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> createInfo(@Valid @RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to save Info : {}", info);
        if (info.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("info", "idexists", "A new info cannot already have an ID")).body(null);
        }
        Info result = infoRepository.save(info);
        infoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("info", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /infos : Updates an existing info.
     *
     * @param info the info to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated info,
     * or with status 400 (Bad Request) if the info is not valid,
     * or with status 500 (Internal Server Error) if the info couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> updateInfo(@Valid @RequestBody Info info) throws URISyntaxException {
        log.debug("REST request to update Info : {}", info);
        if (info.getId() == null) {
            return createInfo(info);
        }
        Info result = infoRepository.save(info);
        infoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("info", info.getId().toString()))
            .body(result);
    }

    /**
     * GET  /infos : get all the infos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of infos in body
     */
    @RequestMapping(value = "/infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Info> getAllInfos() {
        log.debug("REST request to get all Infos");
        List<Info> infos = infoRepository.findAll();
        return infos;
    }

    /**
     * GET  /infos/:id : get the "id" info.
     *
     * @param id the id of the info to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the info, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/infos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Info> getInfo(@PathVariable Long id) {
        log.debug("REST request to get Info : {}", id);
        Info info = infoRepository.findOne(id);
        return Optional.ofNullable(info)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /infos/:id : delete the "id" info.
     *
     * @param id the id of the info to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/infos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInfo(@PathVariable Long id) {
        log.debug("REST request to delete Info : {}", id);
        infoRepository.delete(id);
        infoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("info", id.toString())).build();
    }

    /**
     * SEARCH  /_search/infos?query=:query : search for the info corresponding
     * to the query.
     *
     * @param query the query of the info search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/infos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Info> searchInfos(@RequestParam String query) {
        log.debug("REST request to search Infos for query {}", query);
        return StreamSupport
            .stream(infoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
