package mozhotels.repository.search;

import mozhotels.domain.GuestTourist;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GuestTourist entity.
 */
public interface GuestTouristSearchRepository extends ElasticsearchRepository<GuestTourist, Long> {
}
