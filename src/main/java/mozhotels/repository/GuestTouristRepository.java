package mozhotels.repository;

import mozhotels.domain.GuestTourist;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GuestTourist entity.
 */
@SuppressWarnings("unused")
public interface GuestTouristRepository extends JpaRepository<GuestTourist,Long> {

}
