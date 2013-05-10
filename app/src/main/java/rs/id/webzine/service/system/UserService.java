package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.system.User;
import rs.id.webzine.service.GenericService;

@Component
public class UserService extends GenericService<User> {

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder passwordEncoder;

  private static final String ENCODER_SALT = "myVerySpecialSalt";

  @Transactional
  public void createUser(User user) {
    String encodedPassword = passwordEncoder.encodePassword(user.getPassword(), ENCODER_SALT);

    user.setPassword(encodedPassword);

    super.create(user);
  }

  @Transactional
  public void updateUser(User user) {

    User dbUser = find(user.getId());

    // userName can't be changed (unique key)
    user.setUserName(dbUser.getUserName());

    // password can't be changed (separate action)
    user.setPassword(dbUser.getPassword());

    super.update(user);
  }

  @Transactional
  public void updatePassword(Integer id, String password) {
    User dbUser = find(id);

    String encodedPassword = passwordEncoder.encodePassword(password, ENCODER_SALT);

    dbUser.setPassword(encodedPassword);

    super.update(dbUser);
  }

  /**
   * Find for user name.
   */
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
  public List<User> findForSystem(boolean addEmptyUser) {
    List<User> userList;
    TypedQuery<User> query = entityManager().createQuery("SELECT u FROM User u JOIN u.role r WHERE r.cd != :cd",
        User.class);
    query.setParameter("cd", RoleService.CD_VISITOR);
    userList = query.getResultList();

    if (addEmptyUser) {
      // first entry is empty user
      User emptyUser = new User();
      emptyUser.setId(-1);
      userList.add(0, emptyUser);
    }

    return userList;
  }

}
