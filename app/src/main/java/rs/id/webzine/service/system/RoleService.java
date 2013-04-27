package rs.id.webzine.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.system.Role;
import rs.id.webzine.service.GenericService;

@Component
public class RoleService extends GenericService<Role> {

  public static final String CD_ADMINISTRATOR = "administrator";

  public static final String CD_EDITOR_IN_CHIEF = "editor-00";

  public static final String CD_ASSOCIATE_EDITOR = "editor-01";

  public static final String CD_VISITOR = "visitor";

  public Role findForCd(String cd) {
    Role role = null;

    if (cd != null) {
      TypedQuery<Role> query = entityManager().createQuery("SELECT o FROM Role o WHERE o.cd = :cd", Role.class);
      query.setParameter("cd", cd);
      List<Role> roleList = query.getResultList();
      if (!roleList.isEmpty()) {
        role = roleList.get(0);
      }
    }

    return role;
  }

  public List<Role> findForUserCreate() {
    List<Role> roleList = null;

    // available roles based on user role
    List<String> cdList = new ArrayList<String>();
    if (currentUser().getRole().getCd().equals(CD_ADMINISTRATOR)) {
      cdList.add(CD_ADMINISTRATOR);
      cdList.add(CD_EDITOR_IN_CHIEF);
      cdList.add(CD_ASSOCIATE_EDITOR);
      cdList.add(CD_VISITOR);
    }
    if (currentUser().getRole().getCd().equals(CD_EDITOR_IN_CHIEF)) {
      cdList.add(CD_ASSOCIATE_EDITOR);
    }

    TypedQuery<Role> query = entityManager().createQuery("SELECT o FROM Role o WHERE o.cd IN :cdList", Role.class);
    query.setParameter("cdList", cdList);

    roleList = query.getResultList();

    return roleList;
  }

}
