package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.system.Address;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.GenericService;

@Component
public class UserService extends GenericService<User> {

  @Autowired
  RoleService roleService;

  @Autowired
  AddressService addressService;

  @Autowired
  PasswordEncoder passwordEncoder;

  private static final String ENCODER_SALT = "myVerySpecialSalt";

  @Transactional
  public void create(User user, Address address) {
    addressService.create(address);
    user.setAddress(address);

    String encodedPassword = passwordEncoder.encodePassword(user.getPassword(), ENCODER_SALT);

    user.setPassword(encodedPassword);

    create(user);
  }

  @Transactional
  public void update(Integer userId, User userValues, Address addressValues) {

    User targetUser = find(userId);

    targetUser.setStatus(userValues.getStatus());

    // role can't be changed

    // userName can't be changed (unique key)

    // password can't be changed

    targetUser.setFirstName(userValues.getFirstName());
    targetUser.setLastName(userValues.getLastName());
    targetUser.setBirthdate(userValues.getBirthdate());

    // change image only if provided
    if (userValues.getImage() != null && userValues.getImage() != null && userValues.getImage().length != 0) {
      targetUser.setImage(userValues.getImage());
      targetUser.setImageContentType(userValues.getImageContentType());
    }

    // change address only if provided
    if (addressValues != null) {
      if (targetUser.getAddress() != null) {
        Address targetAddress = targetUser.getAddress();
        targetAddress.setEmail(addressValues.getEmail());
        targetAddress.setPhone(addressValues.getPhone());
        targetAddress.setStreetLine(addressValues.getStreetLine());
        targetAddress.setCity(addressValues.getCity());
        targetAddress.setPostalCode(addressValues.getPostalCode());
        targetAddress.setCountry(addressValues.getCountry());
        targetAddress.setCountryCode(addressValues.getCountryCode());
        targetAddress.setWww(addressValues.getWww());
      } else {
        addressService.create(addressValues);
        targetUser.setAddress(addressValues);
      }
    }

    update(targetUser);
  }

  @Transactional
  public void updatePassword(Integer userId, String password) {
    User targetUser = find(userId);

    String encodedPassword = passwordEncoder.encodePassword(password, ENCODER_SALT);

    targetUser.setPassword(encodedPassword);

    update(targetUser);
  }

  @Transactional
  public void updateRole(Integer userId, String roleCd) {
    User targetUser = find(userId);
    targetUser.setRole(roleService.findForCd(roleCd));

    update(targetUser);
  }

  public User findForUserName(String userName) {
    User user = null;

    if (userName != null) {
      TypedQuery<User> query = entityManager().createQuery("SELECT o FROM User o WHERE o.userName = :userName",
          User.class);
      query.setParameter("userName", userName);
      List<User> userList = query.getResultList();
      if (!userList.isEmpty()) {
        user = userList.get(0);
      }
    }

    return user;
  }

  /**
   * Find all users with the role different from Visitor.
   */
  public List<User> findForSystem() {
    List<User> userList;
    TypedQuery<User> query = entityManager().createQuery("SELECT u FROM User u JOIN u.role r WHERE r.cd != :cd",
        User.class);
    query.setParameter("cd", RoleService.CD_VISITOR);
    userList = query.getResultList();

    User emptyUser = new User();
    emptyUser.setId(-1);
    userList.add(0, emptyUser); // first one is empty user

    return userList;
  }

}
