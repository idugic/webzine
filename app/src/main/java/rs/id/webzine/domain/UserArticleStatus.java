package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "USER_ARTICLE_STATUS")
public class UserArticleStatus extends IdEntity {

  private static Log log = LogFactory.getLog(UserArticleStatus.class);

  public static final String CD_SUBMITTED = "1";

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
    return entityManager().createQuery("SELECT COUNT(o) FROM UserArticleStatus o", Long.class).getSingleResult();
  }

  public static List<UserArticleStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM UserArticleStatus o", UserArticleStatus.class).getResultList();
  }

  public static UserArticleStatus find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(UserArticleStatus.class, id);
  }

  public static List<UserArticleStatus> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM UserArticleStatus o", UserArticleStatus.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }

  public static UserArticleStatus findForCd(String cd) {
    UserArticleStatus articleStatus = null;

    try {
      if (cd != null) {
        TypedQuery<UserArticleStatus> query = entityManager().createQuery(
            "SELECT o FROM UserArticleStatus o WHERE cd = :cd", UserArticleStatus.class);
        query.setParameter("cd", cd);
        articleStatus = query.getSingleResult();
      }
    } catch (Exception e) {
      log.debug(e);
    }

    return articleStatus;
  }
}
