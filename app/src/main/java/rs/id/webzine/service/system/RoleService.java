package rs.id.webzine.service.system;

import java.util.List;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.system.Role;
import rs.id.webzine.service.Service;

@Component
public class RoleService extends Service {

  public List<Role> findAll() {
    return entityManager().createQuery("SELECT o FROM Role o", Role.class).getResultList();
  }

  public Role find(Integer id) {
    return entityManager().find(Role.class, id);
  }

}
