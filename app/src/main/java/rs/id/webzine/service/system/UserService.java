package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.system.Address;
import rs.id.webzine.domain.system.Role;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.GenericService;

@Component
public class UserService extends GenericService<User> {

  // TODO DB based authentication, authorization

  // TODO hash password

  @Autowired
  AddressService addressService;

  @Transactional
  public void create(User user, Address address) {
    addressService.create(address);
    user.setAddress(address);
    create(user);
  }

  @Transactional
  public void update(Integer userId, User user, Address address) {

    User targetUser = find(userId);

    targetUser.setStatus(user.getStatus());

    // role can't be changed (administrator only)

    // userName can't be changed

    // password can't be changed (administrator only)

    targetUser.setFirstName(user.getFirstName());
    targetUser.setLastName(user.getLastName());
    targetUser.setBirthdate(user.getBirthdate());

    // change image only if provided
    if (user.getImage() != null && user.getImage() != null && user.getImage().length != 0) {
      targetUser.setImage(user.getImage());
      targetUser.setImageContentType(user.getImageContentType());
    }

    if (targetUser.getAddress() != null) {
      Address targetAddress = targetUser.getAddress();
      targetAddress.setEmail(address.getEmail());
      targetAddress.setPhone(address.getPhone());
      targetAddress.setStreetLine(address.getStreetLine());
      targetAddress.setCity(address.getCity());
      targetAddress.setPostalCode(address.getPostalCode());
      targetAddress.setCountry(address.getCountry());
      targetAddress.setCountryCode(address.getCountryCode());
      targetAddress.setWww(address.getWww());
    } else {
      addressService.create(address);
      targetUser.setAddress(address);
    }

    update(targetUser);
  }

  @Transactional
  @PreAuthorize("hasRole('administrator')")
  public void updateRole(Integer userId, Role role) {
    User targetUser = find(userId);
    targetUser.setRole(role);

    update(targetUser);
  }

  @Transactional
  @PreAuthorize("hasRole('administrator')")
  public void updatePassword(Integer userId, String password) {
    User targetUser = find(userId);
    targetUser.setPassword(password);

    update(targetUser);
  }

  public User findForUserName(String userName) {
    User user = null;

    if (userName != null) {
      TypedQuery<User> query = entityManager().createQuery("SELECT o FROM User o WHERE userName = :userName",
          User.class);
      query.setParameter("userName", userName);
      List<User> userList = query.getResultList();
      if (!userList.isEmpty()) {
        user = userList.get(0);
      }
    }

    return user;
  }
}
