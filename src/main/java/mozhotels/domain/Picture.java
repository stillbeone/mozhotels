package mozhotels.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import mozhotels.domain.enumeration.PictureType;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "picture_name", nullable = false)
    private String pictureName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PictureType type;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Province province;

    @ManyToOne
    private LocalTur localTur;

    @ManyToOne
    private InstanceTur instanceTur;

    @ManyToOne
    private InstanceRoomType instanceRoomType;

    @ManyToOne
    private InstanceActivity instanceActivity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public PictureType getType() {
        return type;
    }

    public void setType(PictureType type) {
        this.type = type;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public LocalTur getLocalTur() {
        return localTur;
    }

    public void setLocalTur(LocalTur localTur) {
        this.localTur = localTur;
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

    public InstanceActivity getInstanceActivity() {
        return instanceActivity;
    }

    public void setInstanceActivity(InstanceActivity instanceActivity) {
        this.instanceActivity = instanceActivity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Picture picture = (Picture) o;
        if(picture.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, picture.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Picture{" +
            "id=" + id +
            ", pictureName='" + pictureName + "'" +
            ", type='" + type + "'" +
            ", picture='" + picture + "'" +
            ", pictureContentType='" + pictureContentType + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
