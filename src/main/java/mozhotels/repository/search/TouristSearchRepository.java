package mozhotels.repository.search;

import mozhotels.domain.Tourist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Tourist entity.
 */
public interface TouristSearchRepository extends ElasticsearchRepository<Tourist, Long> {
}
