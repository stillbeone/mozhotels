package mozhotels.repository;

import mozhotels.domain.InstanceInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceInfo entity.
 */
@SuppressWarnings("unused")
public interface InstanceInfoRepository extends JpaRepository<InstanceInfo,Long> {

}
