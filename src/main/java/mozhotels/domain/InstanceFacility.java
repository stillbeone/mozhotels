package mozhotels.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import mozhotels.domain.enumeration.InstanceArea;

/**
 * A InstanceFacility.
 */
@Entity
@Table(name = "instance_facility")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancefacility")
public class InstanceFacility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_facility_name", nullable = false)
    private String instanceFacilityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "area")
    private InstanceArea area;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "booking_include")
    private Boolean bookingInclude;

    @ManyToOne
    private InstanceFacilityType instanceFacilityType;

    @ManyToMany(mappedBy = "instanceRoomFacilities")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceRoomType> instanceRoomTypes = new HashSet<>();

    @ManyToOne
    private InstanceTur instanceTur;

    @ManyToMany(mappedBy = "instanceFacilities")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> bookings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceFacilityName() {
        return instanceFacilityName;
    }

    public void setInstanceFacilityName(String instanceFacilityName) {
        this.instanceFacilityName = instanceFacilityName;
    }

    public InstanceArea getArea() {
        return area;
    }

    public void setArea(InstanceArea area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean isBookingInclude() {
        return bookingInclude;
    }

    public void setBookingInclude(Boolean bookingInclude) {
        this.bookingInclude = bookingInclude;
    }

    public InstanceFacilityType getInstanceFacilityType() {
        return instanceFacilityType;
    }

    public void setInstanceFacilityType(InstanceFacilityType instanceFacilityType) {
        this.instanceFacilityType = instanceFacilityType;
    }

    public Set<InstanceRoomType> getInstanceRoomTypes() {
        return instanceRoomTypes;
    }

    public void setInstanceRoomTypes(Set<InstanceRoomType> instanceRoomTypes) {
        this.instanceRoomTypes = instanceRoomTypes;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceFacility instanceFacility = (InstanceFacility) o;
        if(instanceFacility.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceFacility.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceFacility{" +
            "id=" + id +
            ", instanceFacilityName='" + instanceFacilityName + "'" +
            ", area='" + area + "'" +
            ", description='" + description + "'" +
            ", quantity='" + quantity + "'" +
            ", price='" + price + "'" +
            ", bookingInclude='" + bookingInclude + "'" +
            '}';
    }
}
