package mozhotels.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InstanceContact.
 */
@Entity
@Table(name = "instance_contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "instancecontact")
public class InstanceContact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contact_number_principal")
    private Integer contactNumberPrincipal;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(unique = true)
    private InstanceTur instanceTur;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getContactNumberPrincipal() {
        return contactNumberPrincipal;
    }

    public void setContactNumberPrincipal(Integer contactNumberPrincipal) {
        this.contactNumberPrincipal = contactNumberPrincipal;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        InstanceContact instanceContact = (InstanceContact) o;
        if(instanceContact.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, instanceContact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InstanceContact{" +
            "id=" + id +
            ", contactNumberPrincipal='" + contactNumberPrincipal + "'" +
            ", zipCode='" + zipCode + "'" +
            ", address='" + address + "'" +
            ", website='" + website + "'" +
            ", email='" + email + "'" +
            '}';
    }
}
