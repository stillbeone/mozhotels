package mozhotels.repository.search;

import mozhotels.domain.InstanceTurType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceTurType entity.
 */
public interface InstanceTurTypeSearchRepository extends ElasticsearchRepository<InstanceTurType, Long> {
}
