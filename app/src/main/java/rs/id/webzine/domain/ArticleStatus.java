package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ARTICLE_STATUS")
public class ArticleStatus extends IdEntity {

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
    return entityManager().createQuery("SELECT COUNT(o) FROM ArticleStatus o", Long.class).getSingleResult();
  }

  public static List<ArticleStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM ArticleStatus o", ArticleStatus.class).getResultList();
  }

  public static ArticleStatus find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ArticleStatus.class, id);
  }

  public static List<ArticleStatus> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ArticleStatus o", ArticleStatus.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }
}
