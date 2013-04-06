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
@Table(schema = "ADMIN", name = "ARTICLE_COMMENT_STATUS")
public class ArticleCommentStatus extends IdEntity {

  private static Log log = LogFactory.getLog(ArticleCommentStatus.class);

  public static final String CD_DELETED = "0";
  public static final String CD_SUBMITTED = "1";
  public static final String CD_PUBLISHED = "100";

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
    return entityManager().createQuery("SELECT COUNT(o) FROM ArticleCommentStatus o", Long.class).getSingleResult();
  }

  public static List<ArticleCommentStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM ArticleCommentStatus o", ArticleCommentStatus.class)
        .getResultList();
  }

  public static ArticleCommentStatus find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ArticleCommentStatus.class, id);
  }

  public static List<ArticleCommentStatus> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ArticleCommentStatus o", ArticleCommentStatus.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }

  public static ArticleCommentStatus findForCd(String cd) {
    ArticleCommentStatus articleStatus = null;

    try {
      if (cd != null) {
        TypedQuery<ArticleCommentStatus> query = entityManager().createQuery(
            "SELECT o FROM ArticleCommentStatus o WHERE cd = :cd", ArticleCommentStatus.class);
        query.setParameter("cd", cd);
        articleStatus = query.getSingleResult();
      }
    } catch (Exception e) {
      log.debug(e);
    }

    return articleStatus;
  }
}
