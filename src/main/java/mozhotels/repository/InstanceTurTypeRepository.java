package mozhotels.repository;

import mozhotels.domain.InstanceTurType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceTurType entity.
 */
@SuppressWarnings("unused")
public interface InstanceTurTypeRepository extends JpaRepository<InstanceTurType,Long> {

}
