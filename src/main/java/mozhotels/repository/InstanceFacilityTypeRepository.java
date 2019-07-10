package mozhotels.repository;

import mozhotels.domain.InstanceFacilityType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceFacilityType entity.
 */
@SuppressWarnings("unused")
public interface InstanceFacilityTypeRepository extends JpaRepository<InstanceFacilityType,Long> {

}
