package mozhotels.repository.search;

import mozhotels.domain.LocalTur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LocalTur entity.
 */
public interface LocalTurSearchRepository extends ElasticsearchRepository<LocalTur, Long> {
}
