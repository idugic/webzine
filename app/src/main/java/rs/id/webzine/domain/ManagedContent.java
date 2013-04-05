package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(schema = "ADMIN", name = "MANAGED_CONTENT")
@Configurable
public class ManagedContent extends IdEntity {

  @Column(name = "CSS")
  private String css;

  public String getCss() {
    return css;
  }

  public void setCss(String css) {
    this.css = css;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM ManagedContent o", Long.class).getSingleResult();
  }

  public static List<ManagedContent> findAll() {
    return entityManager().createQuery("SELECT o FROM ManagedContent o", ManagedContent.class).getResultList();
  }

  public static ManagedContent find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ManagedContent.class, id);
  }

  public static List<ManagedContent> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ManagedContent o", ManagedContent.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }
  
}
