package mozhotels.service;

import com.codahale.metrics.annotation.Timed;
import mozhotels.domain.*;
import mozhotels.repository.*;
import mozhotels.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private BookingRepository bookingRepository;

    @Inject
    private BookingSearchRepository bookingSearchRepository;

    @Inject
    private BookingPaymentRepository bookingPaymentRepository;

    @Inject
    private BookingPaymentSearchRepository bookingPaymentSearchRepository;

    @Inject
    private CountryRepository countryRepository;

    @Inject
    private CountrySearchRepository countrySearchRepository;

    @Inject
    private FavoriteRepository favoriteRepository;

    @Inject
    private FavoriteSearchRepository favoriteSearchRepository;

    @Inject
    private GuestTouristRepository guestTouristRepository;

    @Inject
    private GuestTouristSearchRepository guestTouristSearchRepository;

    @Inject
    private InfoRepository infoRepository;

    @Inject
    private InfoSearchRepository infoSearchRepository;

    @Inject
    private InstanceActivityRepository instanceActivityRepository;

    @Inject
    private InstanceActivitySearchRepository instanceActivitySearchRepository;

    @Inject
    private InstanceActivityTypeRepository instanceActivityTypeRepository;

    @Inject
    private InstanceActivityTypeSearchRepository instanceActivityTypeSearchRepository;

    @Inject
    private InstanceContactRepository instanceContactRepository;

    @Inject
    private InstanceContactSearchRepository instanceContactSearchRepository;

    @Inject
    private InstanceFacilityRepository instanceFacilityRepository;

    @Inject
    private InstanceFacilitySearchRepository instanceFacilitySearchRepository;

    @Inject
    private InstanceFacilityTypeRepository instanceFacilityTypeRepository;

    @Inject
    private InstanceFacilityTypeSearchRepository instanceFacilityTypeSearchRepository;

    @Inject
    private InstanceInfoRepository instanceInfoRepository;

    @Inject
    private InstanceInfoSearchRepository instanceInfoSearchRepository;

    @Inject
    private InstanceInfoTypeRepository instanceInfoTypeRepository;

    @Inject
    private InstanceInfoTypeSearchRepository instanceInfoTypeSearchRepository;

    @Inject
    private InstanceReviewRepository instanceReviewRepository;

    @Inject
    private InstanceReviewSearchRepository instanceReviewSearchRepository;

    @Inject
    private InstanceRoomTypeRepository instanceRoomTypeRepository;

    @Inject
    private InstanceRoomTypeSearchRepository instanceRoomTypeSearchRepository;

    @Inject
    private InstanceTurRepository instanceTurRepository;

    @Inject
    private InstanceTurSearchRepository instanceTurSearchRepository;

    @Inject
    private InstanceTurTypeRepository instanceTurTypeRepository;

    @Inject
    private InstanceTurTypeSearchRepository instanceTurTypeSearchRepository;

    @Inject
    private LocalTurRepository localTurRepository;

    @Inject
    private LocalTurSearchRepository localTurSearchRepository;

    @Inject
    private PictureRepository pictureRepository;

    @Inject
    private PictureSearchRepository pictureSearchRepository;

    @Inject
    private ProvinceRepository provinceRepository;

    @Inject
    private ProvinceSearchRepository provinceSearchRepository;

    @Inject
    private RegionRepository regionRepository;

    @Inject
    private RegionSearchRepository regionSearchRepository;

    @Inject
    private TouristRepository touristRepository;

    @Inject
    private TouristSearchRepository touristSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Booking.class, bookingRepository, bookingSearchRepository);
        reindexForClass(BookingPayment.class, bookingPaymentRepository, bookingPaymentSearchRepository);
        reindexForClass(Country.class, countryRepository, countrySearchRepository);
        reindexForClass(Favorite.class, favoriteRepository, favoriteSearchRepository);
        reindexForClass(GuestTourist.class, guestTouristRepository, guestTouristSearchRepository);
        reindexForClass(Info.class, infoRepository, infoSearchRepository);
        reindexForClass(InstanceActivity.class, instanceActivityRepository, instanceActivitySearchRepository);
        reindexForClass(InstanceActivityType.class, instanceActivityTypeRepository, instanceActivityTypeSearchRepository);
        reindexForClass(InstanceContact.class, instanceContactRepository, instanceContactSearchRepository);
        reindexForClass(InstanceFacility.class, instanceFacilityRepository, instanceFacilitySearchRepository);
        reindexForClass(InstanceFacilityType.class, instanceFacilityTypeRepository, instanceFacilityTypeSearchRepository);
        reindexForClass(InstanceInfo.class, instanceInfoRepository, instanceInfoSearchRepository);
        reindexForClass(InstanceInfoType.class, instanceInfoTypeRepository, instanceInfoTypeSearchRepository);
        reindexForClass(InstanceReview.class, instanceReviewRepository, instanceReviewSearchRepository);
        reindexForClass(InstanceRoomType.class, instanceRoomTypeRepository, instanceRoomTypeSearchRepository);
        reindexForClass(InstanceTur.class, instanceTurRepository, instanceTurSearchRepository);
        reindexForClass(InstanceTurType.class, instanceTurTypeRepository, instanceTurTypeSearchRepository);
        reindexForClass(LocalTur.class, localTurRepository, localTurSearchRepository);
        reindexForClass(Picture.class, pictureRepository, pictureSearchRepository);
        reindexForClass(Province.class, provinceRepository, provinceSearchRepository);
        reindexForClass(Region.class, regionRepository, regionSearchRepository);
        reindexForClass(Tourist.class, touristRepository, touristSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional
    @SuppressWarnings("unchecked")
    private <T> void reindexForClass(Class<T> entityClass, JpaRepository<T, Long> jpaRepository,
                                                          ElasticsearchRepository<T, Long> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
