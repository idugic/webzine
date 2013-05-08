package rs.id.webzine.entity.system;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", nullable = false)
  @NotNull
  private UserStatus status;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
  @NotNull
  private Role role;

  @Column(name = "USER_NAME", length = 50, unique = true)
  @NotNull
  private String userName;

  @Column(name = "PASSWORD", length = 250)
  @NotNull
  private String password;

  @Column(name = "FIRST_NAME", length = 50)
  private String firstName;

  @Column(name = "LAST_NAME", length = 50)
  private String lastName;

  @Column(name = "IMAGE")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] image;

  @Column(name = "IMAGE_CONTENT_TYPE", length = 100)
  private String imageContentType;

  @ManyToOne
  @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
  private Address address;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public String getImageContentType() {
    return imageContentType;
  }

  public void setImageContentType(String imageContentType) {
    this.imageContentType = imageContentType;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

}
