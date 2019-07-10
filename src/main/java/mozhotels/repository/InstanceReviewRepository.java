package mozhotels.repository;

import mozhotels.domain.InstanceReview;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the InstanceReview entity.
 */
@SuppressWarnings("unused")
public interface InstanceReviewRepository extends JpaRepository<InstanceReview,Long> {

}
