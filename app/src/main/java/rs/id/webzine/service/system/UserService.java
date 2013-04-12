package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.GenericService;

@Component
public class UserService extends GenericService<User> {

  // TODO DB based authentication, authorization

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
