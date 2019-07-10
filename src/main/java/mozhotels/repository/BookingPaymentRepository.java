package mozhotels.repository;

import mozhotels.domain.BookingPayment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BookingPayment entity.
 */
@SuppressWarnings("unused")
public interface BookingPaymentRepository extends JpaRepository<BookingPayment,Long> {

}
