package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.system.Address;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.GenericService;

@Component
public class UserService extends GenericService<User> {

  // TODO DB based authentication, authorization

  // TODO hash password

  @Autowired
  AddressService addressService;

  @Transactional
  public void persist(User user, Address address) {
    addressService.persist(address);
    user.setAddress(address);
    persist(user);
  }

  @Transactional
  public void merge(Integer userId, User user, Address address) {

    User targetUser = find(userId);

    targetUser.setStatus(user.getStatus());
    targetUser.setRole(user.getRole());

    // userName can't be changed

    // password can't be changed

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
      addressService.persist(address);
      targetUser.setAddress(address);
    }

    merge(targetUser);
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
