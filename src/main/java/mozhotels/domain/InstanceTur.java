package mozhotels.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import mozhotels.domain.enumeration.InstanceRating;

import mozhotels.domain.enumeration.Currency;

/**
 * A InstanceTur.
 */
@Entity
@Table(name = "instance_tur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancetur")
public class InstanceTur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_tur_name", nullable = false)
    private String instanceTurName;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private InstanceRating rating;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "rooms")
    private Integer rooms;

    @Column(name = "beds")
    private Integer beds;

    @Column(name = "floors")
    private Integer floors;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Lob
    @Column(name = "photo_principal")
    private byte[] photoPrincipal;

    @Column(name = "photo_principal_content_type")
    private String photoPrincipalContentType;

    @Column(name = "agreement_number")
    private String agreementNumber;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "edit_date")
    private ZonedDateTime editDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "approval")
    private Boolean approval;

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Picture> pictures = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "instance_tur_instance_facility_type",
               joinColumns = @JoinColumn(name="instance_turs_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instance_facility_types_id", referencedColumnName="ID"))
    private Set<InstanceFacilityType> instanceFacilityTypes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "instance_tur_instance_activity_type",
               joinColumns = @JoinColumn(name="instance_turs_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="instance_activity_types_id", referencedColumnName="ID"))
    private Set<InstanceActivityType> instanceActivityTypes = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceFacility> instanceFacilities = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceActivity> instanceActivities = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceRoomType> instanceRoomTypes = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceInfo> instanceInfos = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceReview> instanceReviews = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "instanceTur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Favorite> favorites = new HashSet<>();

    @ManyToOne
    private LocalTur localTur;

    @ManyToOne
    private InstanceTurType instanceTurType;

    @OneToOne(mappedBy = "instanceTur")
    @JsonIgnore
    private InstanceContact instanceContact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceTurName() {
        return instanceTurName;
    }

    public void setInstanceTurName(String instanceTurName) {
        this.instanceTurName = instanceTurName;
    }

    public InstanceRating getRating() {
        return rating;
    }

    public void setRating(InstanceRating rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public byte[] getPhotoPrincipal() {
        return photoPrincipal;
    }

    public void setPhotoPrincipal(byte[] photoPrincipal) {
        this.photoPrincipal = photoPrincipal;
    }

    public String getPhotoPrincipalContentType() {
        return photoPrincipalContentType;
    }

    public void setPhotoPrincipalContentType(String photoPrincipalContentType) {
        this.photoPrincipalContentType = photoPrincipalContentType;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public Set<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<Picture> pictures) {
        this.pictures = pictures;
    }

    public Set<InstanceFacilityType> getInstanceFacilityTypes() {
        return instanceFacilityTypes;
    }

    public void setInstanceFacilityTypes(Set<InstanceFacilityType> instanceFacilityTypes) {
        this.instanceFacilityTypes = instanceFacilityTypes;
    }

    public Set<InstanceActivityType> getInstanceActivityTypes() {
        return instanceActivityTypes;
    }

    public void setInstanceActivityTypes(Set<InstanceActivityType> instanceActivityTypes) {
        this.instanceActivityTypes = instanceActivityTypes;
    }

    public Set<InstanceFacility> getInstanceFacilities() {
        return instanceFacilities;
    }

    public void setInstanceFacilities(Set<InstanceFacility> instanceFacilities) {
        this.instanceFacilities = instanceFacilities;
    }

    public Set<InstanceActivity> getInstanceActivities() {
        return instanceActivities;
    }

    public void setInstanceActivities(Set<InstanceActivity> instanceActivities) {
        this.instanceActivities = instanceActivities;
    }

    public Set<InstanceRoomType> getInstanceRoomTypes() {
        return instanceRoomTypes;
    }

    public void setInstanceRoomTypes(Set<InstanceRoomType> instanceRoomTypes) {
        this.instanceRoomTypes = instanceRoomTypes;
    }

    public Set<InstanceInfo> getInstanceInfos() {
        return instanceInfos;
    }

    public void setInstanceInfos(Set<InstanceInfo> instanceInfos) {
        this.instanceInfos = instanceInfos;
    }

    public Set<InstanceReview> getInstanceReviews() {
        return instanceReviews;
    }

    public void setInstanceReviews(Set<InstanceReview> instanceReviews) {
        this.instanceReviews = instanceReviews;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Set<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Favorite> favorites) {
        this.favorites = favorites;
    }

    public LocalTur getLocalTur() {
        return localTur;
    }

    public void setLocalTur(LocalTur localTur) {
        this.localTur = localTur;
    }

    public InstanceTurType getInstanceTurType() {
        return instanceTurType;
    }

    public void setInstanceTurType(InstanceTurType instanceTurType) {
        this.instanceTurType = instanceTurType;
    }

    public InstanceContact getInstanceContact() {
        return instanceContact;
    }

    public void setInstanceContact(InstanceContact instanceContact) {
        this.instanceContact = instanceContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceTur instanceTur = (InstanceTur) o;
        if(instanceTur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceTur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceTur{" +
            "id=" + id +
            ", instanceTurName='" + instanceTurName + "'" +
            ", rating='" + rating + "'" +
            ", description='" + description + "'" +
            ", latitude='" + latitude + "'" +
            ", longitude='" + longitude + "'" +
            ", rooms='" + rooms + "'" +
            ", beds='" + beds + "'" +
            ", floors='" + floors + "'" +
            ", currency='" + currency + "'" +
            ", photoPrincipal='" + photoPrincipal + "'" +
            ", photoPrincipalContentType='" + photoPrincipalContentType + "'" +
            ", agreementNumber='" + agreementNumber + "'" +
            ", createDate='" + createDate + "'" +
            ", editDate='" + editDate + "'" +
            ", active='" + active + "'" +
            ", approval='" + approval + "'" +
            '}';
    }
}
