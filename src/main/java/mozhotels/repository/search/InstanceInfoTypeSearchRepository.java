package mozhotels.repository.search;

import mozhotels.domain.InstanceInfoType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceInfoType entity.
 */
public interface InstanceInfoTypeSearchRepository extends ElasticsearchRepository<InstanceInfoType, Long> {
}
