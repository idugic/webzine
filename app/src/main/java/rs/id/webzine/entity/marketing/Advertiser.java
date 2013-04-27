package rs.id.webzine.entity.marketing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

import rs.id.webzine.entity.system.Address;

@Entity
@Table(schema = "ADMIN", name = "ADVERTISER")
@Configurable
public class Advertiser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "NAME", length = 100)
  @NotNull
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @ManyToOne
  @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
  private Address address;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

}
