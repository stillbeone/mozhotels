package mozhotels.repository.search;

import mozhotels.domain.InstanceFacilityType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceFacilityType entity.
 */
public interface InstanceFacilityTypeSearchRepository extends ElasticsearchRepository<InstanceFacilityType, Long> {
}
