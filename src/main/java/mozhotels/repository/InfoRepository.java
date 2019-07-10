package mozhotels.repository;

import mozhotels.domain.Info;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Info entity.
 */
@SuppressWarnings("unused")
public interface InfoRepository extends JpaRepository<Info,Long> {

}
