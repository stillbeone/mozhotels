package mozhotels.repository;

import mozhotels.domain.Tourist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tourist entity.
 */
@SuppressWarnings("unused")
public interface TouristRepository extends JpaRepository<Tourist,Long> {

}
