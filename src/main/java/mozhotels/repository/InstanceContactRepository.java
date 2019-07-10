package mozhotels.repository;

import mozhotels.domain.InstanceContact;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceContact entity.
 */
@SuppressWarnings("unused")
public interface InstanceContactRepository extends JpaRepository<InstanceContact,Long> {

}
