package mozhotels.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import mozhotels.domain.enumeration.InfoType;

/**
 * A InstanceInfo.
 */
@Entity
@Table(name = "instance_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceinfo")
public class InstanceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "instance_info", nullable = false)
    private InfoType instanceInfo;

    @NotNull
    @Column(name = "instance_info_name", nullable = false)
    private String instanceInfoName;

    @NotNull
    @Column(name = "info", nullable = false)
    private String info;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private InstanceInfoType instanceInfoType;

    @ManyToOne
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfoType getInstanceInfo() {
        return instanceInfo;
    }

    public void setInstanceInfo(InfoType instanceInfo) {
        this.instanceInfo = instanceInfo;
    }

    public String getInstanceInfoName() {
        return instanceInfoName;
    }

    public void setInstanceInfoName(String instanceInfoName) {
        this.instanceInfoName = instanceInfoName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InstanceInfoType getInstanceInfoType() {
        return instanceInfoType;
    }

    public void setInstanceInfoType(InstanceInfoType instanceInfoType) {
        this.instanceInfoType = instanceInfoType;
    }

    public InstanceTur getInstanceTur() {
        return instanceTur;
    }

    public void setInstanceTur(InstanceTur instanceTur) {
        this.instanceTur = instanceTur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceInfo instanceInfo = (InstanceInfo) o;
        if(instanceInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceInfo{" +
            "id=" + id +
            ", instanceInfo='" + instanceInfo + "'" +
            ", instanceInfoName='" + instanceInfoName + "'" +
            ", info='" + info + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
