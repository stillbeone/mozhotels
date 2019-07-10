package mozhotels.repository.search;

import mozhotels.domain.InstanceReview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceReview entity.
 */
public interface InstanceReviewSearchRepository extends ElasticsearchRepository<InstanceReview, Long> {
}
