package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.Province;
import mozhotels.repository.ProvinceRepository;
import mozhotels.repository.search.ProvinceSearchRepository;
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
 * REST controller for managing Province.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

    private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);
        
    @Inject
    private ProvinceRepository provinceRepository;
    
    @Inject
    private ProvinceSearchRepository provinceSearchRepository;
    
    /**
     * POST  /provinces : Create a new province.
     *
     * @param province the province to create
     * @return the ResponseEntity with status 201 (Created) and with body the new province, or with status 400 (Bad Request) if the province has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Province> createProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to save Province : {}", province);
        if (province.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("province", "idexists", "A new province cannot already have an ID")).body(null);
        }
        Province result = provinceRepository.save(province);
        provinceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("province", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /provinces : Updates an existing province.
     *
     * @param province the province to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated province,
     * or with status 400 (Bad Request) if the province is not valid,
     * or with status 500 (Internal Server Error) if the province couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Province> updateProvince(@Valid @RequestBody Province province) throws URISyntaxException {
        log.debug("REST request to update Province : {}", province);
        if (province.getId() == null) {
            return createProvince(province);
        }
        Province result = provinceRepository.save(province);
        provinceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("province", province.getId().toString()))
            .body(result);
    }

    /**
     * GET  /provinces : get all the provinces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of provinces in body
     */
    @RequestMapping(value = "/provinces",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Province> getAllProvinces() {
        log.debug("REST request to get all Provinces");
        List<Province> provinces = provinceRepository.findAll();
        return provinces;
    }

    /**
     * GET  /provinces/:id : get the "id" province.
     *
     * @param id the id of the province to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the province, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/provinces/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Province> getProvince(@PathVariable Long id) {
        log.debug("REST request to get Province : {}", id);
        Province province = provinceRepository.findOne(id);
        return Optional.ofNullable(province)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /provinces/:id : delete the "id" province.
     *
     * @param id the id of the province to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/provinces/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        log.debug("REST request to delete Province : {}", id);
        provinceRepository.delete(id);
        provinceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("province", id.toString())).build();
    }

    /**
     * SEARCH  /_search/provinces?query=:query : search for the province corresponding
     * to the query.
     *
     * @param query the query of the province search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/provinces",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Province> searchProvinces(@RequestParam String query) {
        log.debug("REST request to search Provinces for query {}", query);
        return StreamSupport
            .stream(provinceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
