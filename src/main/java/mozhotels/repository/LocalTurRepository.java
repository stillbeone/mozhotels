package mozhotels.repository;

import mozhotels.domain.LocalTur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LocalTur entity.
 */
@SuppressWarnings("unused")
public interface LocalTurRepository extends JpaRepository<LocalTur,Long> {

}
