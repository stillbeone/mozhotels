package mozhotels.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import mozhotels.domain.enumeration.InfoType;

/**
 * A InstanceInfoType.
 */
@Entity
@Table(name = "instance_info_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceinfotype")
public class InstanceInfoType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "instance_info_type", nullable = false)
    private InfoType instanceInfoType;

    @NotNull
    @Column(name = "instance_info_type_name", nullable = false)
    private String instanceInfoTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "instanceInfoType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceInfo> instanceInfos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfoType getInstanceInfoType() {
        return instanceInfoType;
    }

    public void setInstanceInfoType(InfoType instanceInfoType) {
        this.instanceInfoType = instanceInfoType;
    }

    public String getInstanceInfoTypeName() {
        return instanceInfoTypeName;
    }

    public void setInstanceInfoTypeName(String instanceInfoTypeName) {
        this.instanceInfoTypeName = instanceInfoTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InstanceInfo> getInstanceInfos() {
        return instanceInfos;
    }

    public void setInstanceInfos(Set<InstanceInfo> instanceInfos) {
        this.instanceInfos = instanceInfos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceInfoType instanceInfoType = (InstanceInfoType) o;
        if(instanceInfoType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceInfoType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceInfoType{" +
            "id=" + id +
            ", instanceInfoType='" + instanceInfoType + "'" +
            ", instanceInfoTypeName='" + instanceInfoTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
