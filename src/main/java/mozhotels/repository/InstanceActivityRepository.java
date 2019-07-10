package mozhotels.repository;

import mozhotels.domain.InstanceActivity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceActivity entity.
 */
@SuppressWarnings("unused")
public interface InstanceActivityRepository extends JpaRepository<InstanceActivity,Long> {

}
