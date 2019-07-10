package mozhotels.repository.search;

import mozhotels.domain.InstanceFacility;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceFacility entity.
 */
public interface InstanceFacilitySearchRepository extends ElasticsearchRepository<InstanceFacility, Long> {
}
