package mozhotels.repository;

import mozhotels.domain.InstanceRoomType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceRoomType entity.
 */
@SuppressWarnings("unused")
public interface InstanceRoomTypeRepository extends JpaRepository<InstanceRoomType,Long> {

    @Query("select distinct instanceRoomType from InstanceRoomType instanceRoomType left join fetch instanceRoomType.instanceRoomFacilities")
    List<InstanceRoomType> findAllWithEagerRelationships();

    @Query("select instanceRoomType from InstanceRoomType instanceRoomType left join fetch instanceRoomType.instanceRoomFacilities where instanceRoomType.id =:id")
    InstanceRoomType findOneWithEagerRelationships(@Param("id") Long id);

}
