package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

import rs.id.webzine.domain.system.Address;

@Entity
@Table(schema = "ADMIN", name = "CUSTOMER")
@Configurable
public class Customer extends IdEntity {

  @Column(name = "NAME", length = 100)
  @NotNull
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @ManyToOne
  @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
  private Address addressId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Address getAddressId() {
    return addressId;
  }

  public void setAddressId(Address addressId) {
    this.addressId = addressId;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM Customer o", Long.class).getSingleResult();
  }

  public static List<Customer> findAll() {
    return entityManager().createQuery("SELECT o FROM Customer o", Customer.class).getResultList();
  }

  public static Customer find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(Customer.class, id);
  }

  public static List<Customer> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM Customer o", Customer.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

}
