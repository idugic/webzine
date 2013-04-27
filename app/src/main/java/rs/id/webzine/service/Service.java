package rs.id.webzine.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.system.User;

@Configurable
public class Service {
  
  // TODO remove

  @PersistenceContext
  transient EntityManager entityManager;

  public static final EntityManager entityManager() {
    EntityManager em = new Service().entityManager;
    if (em == null)
      throw new IllegalStateException("Entity manager has not been injected.");
    return em;
  }

  @Transactional
  public void persist(Object entity) {
    entityManager().persist(entity);
  }

  @Transactional
  public void merge(Object entity) {
    entityManager().merge(entity);
    entityManager().flush();
  }

  @Transactional
  public void remove(Object entity) {
    entityManager().remove(entity);
  }

  @Transactional
  public void flush() {
    entityManager().flush();
  }

  @Transactional
  public void clear() {
    entityManager().clear();
  }

  public static User getCurrentUser() {
    User user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    if (userName != null) {
      // user = User.findForUserName(userName);
    }

    return user;
  }
}
