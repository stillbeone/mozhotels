package mozhotels.repository.search;

import mozhotels.domain.InstanceActivity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceActivity entity.
 */
public interface InstanceActivitySearchRepository extends ElasticsearchRepository<InstanceActivity, Long> {
}
