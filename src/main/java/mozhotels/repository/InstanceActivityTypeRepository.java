package mozhotels.repository;

import mozhotels.domain.InstanceActivityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceActivityType entity.
 */
@SuppressWarnings("unused")
public interface InstanceActivityTypeRepository extends JpaRepository<InstanceActivityType,Long> {

}
