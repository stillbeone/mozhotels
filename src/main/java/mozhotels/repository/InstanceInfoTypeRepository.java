package mozhotels.repository;

import mozhotels.domain.InstanceInfoType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceInfoType entity.
 */
@SuppressWarnings("unused")
public interface InstanceInfoTypeRepository extends JpaRepository<InstanceInfoType,Long> {

}
