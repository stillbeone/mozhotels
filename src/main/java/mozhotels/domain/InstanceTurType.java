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
 * A InstanceTurType.
 */
@Entity
@Table(name = "instance_tur_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instanceturtype")
public class InstanceTurType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "instance_tur_type_name", nullable = false)
    private String instanceTurTypeName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "instanceTurType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InstanceTur> instanceTurs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceTurTypeName() {
        return instanceTurTypeName;
    }

    public void setInstanceTurTypeName(String instanceTurTypeName) {
        this.instanceTurTypeName = instanceTurTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        InstanceTurType instanceTurType = (InstanceTurType) o;
        if(instanceTurType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceTurType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceTurType{" +
            "id=" + id +
            ", instanceTurTypeName='" + instanceTurTypeName + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
