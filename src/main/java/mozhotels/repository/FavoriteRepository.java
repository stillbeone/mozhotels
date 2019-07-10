package mozhotels.repository;

import mozhotels.domain.Favorite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Favorite entity.
 */
@SuppressWarnings("unused")
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

}
