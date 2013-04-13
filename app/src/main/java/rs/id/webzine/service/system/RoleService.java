package rs.id.webzine.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.system.Role;
import rs.id.webzine.service.GenericService;

@Component
public class RoleService extends GenericService<Role> {

  public static String ADMINISTRATOR = "0";

  public static String EDITOR_IN_CHIEF = "1";

  public static String ASSOCIATE_EDITOR = "2";

  public static String VISITOR = "100";

  public List<Role> findAllAvailableForUser() {
    List<Role> roleList = null;

    List<String> cdList = getAvailableRoleCdList();

    TypedQuery<Role> query = entityManager().createQuery("SELECT o FROM Role o WHERE o.cd IN :cdList", Role.class);
    query.setParameter("cdList", cdList);

    roleList = query.getResultList();

    return roleList;
  }

  public List<String> getAvailableRoleCdList() {
    List<String> cdList = new ArrayList<String>();

    if (currentUser().getRole().getCd().equals(ADMINISTRATOR)) {
      cdList.add(ADMINISTRATOR);
      cdList.add(EDITOR_IN_CHIEF);
    }

    cdList.add(ASSOCIATE_EDITOR);
    cdList.add(VISITOR);

    return cdList;
  }
}
