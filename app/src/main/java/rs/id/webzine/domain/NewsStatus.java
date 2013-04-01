package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "NEWS_STATUS")
public class NewsStatus extends IdEntity {

  @Column(name = "CD", length = 15, unique = true)
  @NotNull
  private String cd;

  @Column(name = "NAME", length = 50)
  @NotNull
  private String name;

  public String getCd() {
    return cd;
  }

  public void setCd(String cd) {
    this.cd = cd;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM NewsStatus o", Long.class).getSingleResult();
  }

  public static List<NewsStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM NewsStatus o", NewsStatus.class).getResultList();
  }

  public static NewsStatus find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(NewsStatus.class, id);
  }

  public static List<NewsStatus> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM NewsStatus o", NewsStatus.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }
}
