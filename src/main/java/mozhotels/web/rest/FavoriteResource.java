package mozhotels.web.rest;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.Favorite;
import mozhotels.repository.FavoriteRepository;
import mozhotels.repository.search.FavoriteSearchRepository;
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
 * REST controller for managing Favorite.
 */
@RestController
@RequestMapping("/api")
public class FavoriteResource {

    private final Logger log = LoggerFactory.getLogger(FavoriteResource.class);
        
    @Inject
    private FavoriteRepository favoriteRepository;
    
    @Inject
    private FavoriteSearchRepository favoriteSearchRepository;
    
    /**
     * POST  /favorites : Create a new favorite.
     *
     * @param favorite the favorite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favorite, or with status 400 (Bad Request) if the favorite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/favorites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorite> createFavorite(@RequestBody Favorite favorite) throws URISyntaxException {
        log.debug("REST request to save Favorite : {}", favorite);
        if (favorite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favorite", "idexists", "A new favorite cannot already have an ID")).body(null);
        }
        Favorite result = favoriteRepository.save(favorite);
        favoriteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favorite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favorites : Updates an existing favorite.
     *
     * @param favorite the favorite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favorite,
     * or with status 400 (Bad Request) if the favorite is not valid,
     * or with status 500 (Internal Server Error) if the favorite couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/favorites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorite> updateFavorite(@RequestBody Favorite favorite) throws URISyntaxException {
        log.debug("REST request to update Favorite : {}", favorite);
        if (favorite.getId() == null) {
            return createFavorite(favorite);
        }
        Favorite result = favoriteRepository.save(favorite);
        favoriteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favorite", favorite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favorites : get all the favorites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favorites in body
     */
    @RequestMapping(value = "/favorites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Favorite> getAllFavorites() {
        log.debug("REST request to get all Favorites");
        List<Favorite> favorites = favoriteRepository.findAll();
        return favorites;
    }

    /**
     * GET  /favorites/:id : get the "id" favorite.
     *
     * @param id the id of the favorite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favorite, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/favorites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorite> getFavorite(@PathVariable Long id) {
        log.debug("REST request to get Favorite : {}", id);
        Favorite favorite = favoriteRepository.findOne(id);
        return Optional.ofNullable(favorite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favorites/:id : delete the "id" favorite.
     *
     * @param id the id of the favorite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/favorites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        log.debug("REST request to delete Favorite : {}", id);
        favoriteRepository.delete(id);
        favoriteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favorite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/favorites?query=:query : search for the favorite corresponding
     * to the query.
     *
     * @param query the query of the favorite search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/favorites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Favorite> searchFavorites(@RequestParam String query) {
        log.debug("REST request to search Favorites for query {}", query);
        return StreamSupport
            .stream(favoriteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
