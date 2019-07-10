package mozhotels.repository.search;

import mozhotels.domain.BookingPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BookingPayment entity.
 */
public interface BookingPaymentSearchRepository extends ElasticsearchRepository<BookingPayment, Long> {
}
