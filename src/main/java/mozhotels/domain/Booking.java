package mozhotels.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import mozhotels.domain.enumeration.BookingState;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @NotNull
    @Column(name = "people_adult", nullable = false)
    private Integer peopleAdult;

    @NotNull
    @Column(name = "people_child", nullable = false)
    private Integer peopleChild;

    @NotNull
    @Column(name = "rooms", nullable = false)
    private Integer rooms;

    @NotNull
    @Column(name = "tax", nullable = false)
    private Float tax;

    @NotNull
    @Column(name = "total_price", precision=10, scale=2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private BookingState state;

    @Size(max = 1024)
    @Column(name = "notes", length = 1024)
    private String notes;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime createDate;

    @Column(name = "edit_date")
    private ZonedDateTime editDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "booking_instance_facility",
               joinColumns = @JoinColumn(name="bookings_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instance_facilities_id", referencedColumnName="ID"))
    private Set<InstanceFacility> instanceFacilities = new HashSet<>();

    @ManyToOne
    private Tourist tourist;

    @ManyToOne
    private GuestTourist guestTourist;

    @ManyToOne
    private InstanceTur instanceTur;

    @ManyToOne
    private InstanceRoomType instanceRoomType;

    @OneToOne(mappedBy = "booking")
    @JsonIgnore
    private BookingPayment bookingPayment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getPeopleAdult() {
        return peopleAdult;
    }

    public void setPeopleAdult(Integer peopleAdult) {
        this.peopleAdult = peopleAdult;
    }

    public Integer getPeopleChild() {
        return peopleChild;
    }

    public void setPeopleChild(Integer peopleChild) {
        this.peopleChild = peopleChild;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingState getState() {
        return state;
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public ZonedDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(ZonedDateTime editDate) {
        this.editDate = editDate;
    }

    public Set<InstanceFacility> getInstanceFacilities() {
        return instanceFacilities;
    }

    public void setInstanceFacilities(Set<InstanceFacility> instanceFacilities) {
        this.instanceFacilities = instanceFacilities;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    public GuestTourist getGuestTourist() {
        return guestTourist;
    }

    public void setGuestTourist(GuestTourist guestTourist) {
        this.guestTourist = guestTourist;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    public InstanceRoomType getInstanceRoomType() {
        return instanceRoomType;
    }

    public void setInstanceRoomType(InstanceRoomType instanceRoomType) {
        this.instanceRoomType = instanceRoomType;
    }

    public BookingPayment getBookingPayment() {
        return bookingPayment;
    }

    public void setBookingPayment(BookingPayment bookingPayment) {
        this.bookingPayment = bookingPayment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;
        if(booking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + id +
            ", checkIn='" + checkIn + "'" +
            ", checkOut='" + checkOut + "'" +
            ", peopleAdult='" + peopleAdult + "'" +
            ", peopleChild='" + peopleChild + "'" +
            ", rooms='" + rooms + "'" +
            ", tax='" + tax + "'" +
            ", totalPrice='" + totalPrice + "'" +
            ", state='" + state + "'" +
            ", notes='" + notes + "'" +
            ", createDate='" + createDate + "'" +
            ", editDate='" + editDate + "'" +
            '}';
    }
}
