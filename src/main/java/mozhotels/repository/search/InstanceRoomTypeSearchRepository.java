package mozhotels.repository.search;

import mozhotels.domain.InstanceRoomType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the InstanceRoomType entity.
 */
public interface InstanceRoomTypeSearchRepository extends ElasticsearchRepository<InstanceRoomType, Long> {
}
