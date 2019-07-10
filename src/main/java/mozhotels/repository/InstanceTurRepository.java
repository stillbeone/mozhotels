package mozhotels.repository;

import mozhotels.domain.InstanceTur;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceTur entity.
 */
@SuppressWarnings("unused")
public interface InstanceTurRepository extends JpaRepository<InstanceTur,Long> {

    @Query("select distinct instanceTur from InstanceTur instanceTur left join fetch instanceTur.instanceFacilityTypes left join fetch instanceTur.instanceActivityTypes")
    List<InstanceTur> findAllWithEagerRelationships();

    @Query("select instanceTur from InstanceTur instanceTur left join fetch instanceTur.instanceFacilityTypes left join fetch instanceTur.instanceActivityTypes where instanceTur.id =:id")
    InstanceTur findOneWithEagerRelationships(@Param("id") Long id);



}
