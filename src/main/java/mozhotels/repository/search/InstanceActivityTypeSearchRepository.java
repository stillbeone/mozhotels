package mozhotels.repository.search;

import mozhotels.domain.InstanceActivityType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceActivityType entity.
 */
public interface InstanceActivityTypeSearchRepository extends ElasticsearchRepository<InstanceActivityType, Long> {
}
