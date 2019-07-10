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

/**
 * A InstanceActivityType.
 */
@Entity
@Table(name = "instance_activity_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceactivitytype")
public class InstanceActivityType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_activity_type_name", nullable = false)
    private String instanceActivityTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "instanceActivityType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceActivity> instanceActivities = new HashSet<>();

    @ManyToMany(mappedBy = "instanceActivityTypes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceTur> instanceTurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceActivityTypeName() {
        return instanceActivityTypeName;
    }

    public void setInstanceActivityTypeName(String instanceActivityTypeName) {
        this.instanceActivityTypeName = instanceActivityTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InstanceActivity> getInstanceActivities() {
        return instanceActivities;
    }

    public void setInstanceActivities(Set<InstanceActivity> instanceActivities) {
        this.instanceActivities = instanceActivities;
    }

    public Set<InstanceTur> getInstanceTurs() {
        return instanceTurs;
    }

    public void setInstanceTurs(Set<InstanceTur> instanceTurs) {
        this.instanceTurs = instanceTurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InstanceActivityType instanceActivityType = (InstanceActivityType) o;
        if(instanceActivityType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceActivityType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceActivityType{" +
            "id=" + id +
            ", instanceActivityTypeName='" + instanceActivityTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
