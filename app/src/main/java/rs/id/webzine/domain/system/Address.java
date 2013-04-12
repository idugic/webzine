package rs.id.webzine.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ADDRESS")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "EMAIL", length = 100)
  private String email;

  @Column(name = "PHONE", length = 50)
  private String phone;

  @Column(name = "STREET_LINE", length = 100)
  private String streetLine;

  @Column(name = "CITY", length = 50)
  private String city;

  @Column(name = "POSTAL_CODE", length = 15)
  private String postalCode;

  @Column(name = "COUNTRY", length = 50)
  private String country;

  @Column(name = "COUNTRY_CODE", length = 15)
  private String countryCode;

  @Column(name = "WWW", length = 250)
  private String www;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStreetLine() {
    return streetLine;
  }

  public void setStreetLine(String streetLine) {
    this.streetLine = streetLine;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getWww() {
    return www;
  }

  public void setWww(String www) {
    this.www = www;
  }

}
