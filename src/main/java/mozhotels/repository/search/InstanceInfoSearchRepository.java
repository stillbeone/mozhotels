package mozhotels.repository.search;

import mozhotels.domain.InstanceInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceInfo entity.
 */
public interface InstanceInfoSearchRepository extends ElasticsearchRepository<InstanceInfo, Long> {
}
