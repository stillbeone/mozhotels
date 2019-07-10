package mozhotels.repository.search;

import mozhotels.domain.Province;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Province entity.
 */
public interface ProvinceSearchRepository extends ElasticsearchRepository<Province, Long> {
}
