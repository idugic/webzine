package rs.id.webzine.controller.system;

import rs.id.webzine.entity.system.Country;
import rs.id.webzine.entity.system.Role;
import rs.id.webzine.entity.system.UserStatus;

public class UserForm {

  // form
  private Integer userId;

  // user
  private Role role;

  private UserStatus status;

  private String userName;

  private String password;

  private String firstName;

  private String lastName;

  private byte[] image;

  private String imageContentType;

  private String imageUrl;

  // address
  private String email;

  private String phone;

  private String streetLine;

  private String city;

  private String postalCode;

  private Country country;

  private String www;

  // change role
  private Role newRole;

  // change password
  private String newPassword;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
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

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
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

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public String getWww() {
    return www;
  }

  public void setWww(String www) {
    this.www = www;
  }

  public Role getNewRole() {
    return newRole;
  }

  public void setNewRole(Role newRole) {
    this.newRole = newRole;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

}
