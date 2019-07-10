package mozhotels.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import mozhotels.domain.enumeration.Evaluation;

/**
 * A InstanceReview.
 */
@Entity
@Table(name = "instance_review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancereview")
public class InstanceReview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "cleanliness", nullable = false)
    private Float cleanliness;

    @NotNull
    @Column(name = "room_confort", nullable = false)
    private Float roomConfort;

    @NotNull
    @Column(name = "location", nullable = false)
    private Float location;

    @NotNull
    @Column(name = "service_staff", nullable = false)
    private Float serviceStaff;

    @NotNull
    @Column(name = "sleep_quality", nullable = false)
    private Float sleepQuality;

    @NotNull
    @Column(name = "value_price", nullable = false)
    private Float valuePrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation", nullable = false)
    private Evaluation evaluation;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "create_date")
    private ZonedDateTime createDate;

    @Column(name = "edit_date")
    private ZonedDateTime editDate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "approval")
    private Boolean approval;

    @ManyToOne
    private InstanceTur instanceTur;

    @ManyToOne
    private Tourist tourist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(Float cleanliness) {
        this.cleanliness = cleanliness;
    }

    public Float getRoomConfort() {
        return roomConfort;
    }

    public void setRoomConfort(Float roomConfort) {
        this.roomConfort = roomConfort;
    }

    public Float getLocation() {
        return location;
    }

    public void setLocation(Float location) {
        this.location = location;
    }

    public Float getServiceStaff() {
        return serviceStaff;
    }

    public void setServiceStaff(Float serviceStaff) {
        this.serviceStaff = serviceStaff;
    }

    public Float getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Float sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public Float getValuePrice() {
        return valuePrice;
    }

    public void setValuePrice(Float valuePrice) {
        this.valuePrice = valuePrice;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public void setTourist(Tourist tourist) {
        this.tourist = tourist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceReview instanceReview = (InstanceReview) o;
        if(instanceReview.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceReview.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceReview{" +
            "id=" + id +
            ", cleanliness='" + cleanliness + "'" +
            ", roomConfort='" + roomConfort + "'" +
            ", location='" + location + "'" +
            ", serviceStaff='" + serviceStaff + "'" +
            ", sleepQuality='" + sleepQuality + "'" +
            ", valuePrice='" + valuePrice + "'" +
            ", evaluation='" + evaluation + "'" +
            ", title='" + title + "'" +
            ", comment='" + comment + "'" +
            ", createDate='" + createDate + "'" +
            ", editDate='" + editDate + "'" +
            ", active='" + active + "'" +
            ", approval='" + approval + "'" +
            '}';
    }
}
