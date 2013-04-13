package rs.id.webzine.service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.system.User;

@Configurable
public class GenericService<E> {

  private Class<E> type;

  @SuppressWarnings("unchecked")
  public GenericService() {
    Type t = getClass().getGenericSuperclass();
    ParameterizedType pt = (ParameterizedType) t;
    type = (Class<E>) pt.getActualTypeArguments()[0];
  }

  @PersistenceContext
  transient EntityManager entityManager;

  public final EntityManager entityManager() {
    return entityManager;
  }

  @Transactional
  public void create(E entity) {
    entityManager().persist(entity);
  }

  @Transactional
  public void update(E entity) {
    entityManager().merge(entity);
    entityManager().flush();
  }

  @Transactional
  public void delete(Integer id) {
    entityManager().remove(find(id));
  }

  @Transactional
  protected void flushEntityManager() {
    entityManager().flush();
  }

  @Transactional
  protected void clearEntityManager() {
    entityManager().clear();
  }

  public long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM " + type.getName() + " o", Long.class).getSingleResult();
  }

  public List<E> findAll() {
    return entityManager().createQuery("SELECT o FROM " + type.getName() + " o", type).getResultList();
  }

  public E find(Integer id) {
    return entityManager().find(type, id);
  }

  public List<E> findForList(int firstResult, int maxResults) {
    List<E> list = new ArrayList<E>();
    TypedQuery<E> query = entityManager().createQuery("SELECT o FROM " + type.getName() + " o", type)
        .setFirstResult(firstResult).setMaxResults(maxResults);
    list = query.getResultList();
    return list;
  }

  public User currentUser() {
    User user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();

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
