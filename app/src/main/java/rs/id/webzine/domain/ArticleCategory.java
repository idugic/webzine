package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import rs.id.webzine.domain.system.User;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ARTICLE_CATEGORY")
public class ArticleCategory extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
  private Article articleId;

  @ManyToOne
  @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
  private Category categoryId;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  private User uc;

  @Column(name = "DC")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dc;

  @Column(name = "DM")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dm;

  public Article getArticleId() {
    return articleId;
  }

  public void setArticleId(Article articleId) {
    this.articleId = articleId;
  }

  public Category getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Category categoryId) {
    this.categoryId = categoryId;
  }

  public User getUm() {
    return um;
  }

  public void setUm(User um) {
    this.um = um;
  }

  public User getUc() {
    return uc;
  }

  public void setUc(User uc) {
    this.uc = uc;
  }

  public Calendar getDc() {
    return dc;
  }

  public void setDc(Calendar dc) {
    this.dc = dc;
  }

  public Calendar getDm() {
    return dm;
  }

  public void setDm(Calendar dm) {
    this.dm = dm;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM ArticleCategory o", Long.class).getSingleResult();
  }

  public static List<ArticleCategory> findAll() {
    return entityManager().createQuery("SELECT o FROM ArticleCategory o", ArticleCategory.class).getResultList();
  }

  public static ArticleCategory find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ArticleCategory.class, id);
  }

  public static List<ArticleCategory> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ArticleCategory o", ArticleCategory.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }

  public static List<ArticleCategory> findForArticle(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      TypedQuery<ArticleCategory> query = entityManager().createQuery(
          "SELECT ac FROM ArticleCategory ac JOIN ac.articleId a WHERE a.id = :articleId", ArticleCategory.class);
      query.setParameter("articleId", articleId);
      return query.getResultList();
    }
  }
}
