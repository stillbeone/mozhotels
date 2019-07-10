package mozhotels.repository;

import mozhotels.domain.InstanceFacility;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceFacility entity.
 */
@SuppressWarnings("unused")
public interface InstanceFacilityRepository extends JpaRepository<InstanceFacility,Long> {

}
