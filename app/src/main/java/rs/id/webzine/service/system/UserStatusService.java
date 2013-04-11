package rs.id.webzine.service.system;

import java.util.List;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.system.UserStatus;
import rs.id.webzine.service.Service;

@Component
public class UserStatusService extends Service {

  public List<UserStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM UserStatus o", UserStatus.class).getResultList();
  }

  public UserStatus find(Integer id) {
    return entityManager().find(UserStatus.class, id);
  }
}
