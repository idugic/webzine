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
import rs.id.webzine.domain.util.Session;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ARTICLE_COMMENT")
public class ArticleComment extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
  private Article articleId;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private ArticleCommentStatus statusId;

  @Column(name = "TEXT", length = 250)
  @NotNull
  private String text;

  @ManyToOne
  @JoinColumn(name = "PUBLISHED_BY", referencedColumnName = "ID")
  private User publishedBy;

  @Column(name = "PUBLISHED_AT")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar publishedAt;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  private User uc;

  @Column(name = "DC")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dc;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

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

  public ArticleCommentStatus getStatusId() {
    return statusId;
  }

  public void setStatusId(ArticleCommentStatus statusId) {
    this.statusId = statusId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public User getPublishedBy() {
    return publishedBy;
  }

  public void setPublishedBy(User publishedBy) {
    this.publishedBy = publishedBy;
  }

  public Calendar getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Calendar publishedAt) {
    this.publishedAt = publishedAt;
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

  public User getUm() {
    return um;
  }

  public void setUm(User um) {
    this.um = um;
  }

  public Calendar getDm() {
    return dm;
  }

  public void setDm(Calendar dm) {
    this.dm = dm;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM ArticleComment o", Long.class).getSingleResult();
  }

  public static List<ArticleComment> findAll() {
    return entityManager().createQuery("SELECT o FROM ArticleComment o", ArticleComment.class).getResultList();
  }

  public static ArticleComment find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ArticleComment.class, id);
  }

  public static List<ArticleComment> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ArticleComment o", ArticleComment.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }

  public static List<ArticleComment> findForArticle(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      TypedQuery<ArticleComment> query = entityManager()
          .createQuery(
              "SELECT ac FROM ArticleComment ac JOIN ac.articleId a JOIN ac.statusId s WHERE s.cd <> '0' AND a.id = :articleId",
              ArticleComment.class);
      query.setParameter("articleId", articleId);
      return query.getResultList();
    }
  }

  public static void publish(Integer id) {
    ArticleComment articleComment = find(id);
    articleComment.setStatusId(ArticleCommentStatus.findForCd(ArticleCommentStatus.CD_PUBLISHED));
    articleComment.setPublishedBy(Session.getCurrentUser());
    articleComment.setPublishedAt(Calendar.getInstance());
    articleComment.merge();
  }
}
