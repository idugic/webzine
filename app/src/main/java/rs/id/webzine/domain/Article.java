package rs.id.webzine.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import rs.id.webzine.domain.system.User;
import rs.id.webzine.domain.util.Session;

@Entity
@Table(schema = "ADMIN", name = "ARTICLE")
@Configurable
public class Article extends IdEntity {

  @Transient
  String abstractMediaUrl;

  @Column(name = "TITLE", length = 200)
  @NotNull
  private String title;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private ArticleStatus statusId;

  @Column(name = "ABSTRACT_TEXT", length = 500)
  private String abstractText;

  @Column(name = "ABSTRACT_MEDIA")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] abstractMedia;

  @Column(name = "ABSTRACT_MEDIA_CONTENT_TYPE", length = 200)
  @NotNull
  private String abstractMediaContentType;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContentId;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ArticleStatus getStatusId() {
    return statusId;
  }

  public void setStatusId(ArticleStatus statusId) {
    this.statusId = statusId;
  }

  public String getAbstractText() {
    return abstractText;
  }

  public void setAbstractText(String abstractText) {
    this.abstractText = abstractText;
  }

  public byte[] getAbstractMedia() {
    return abstractMedia;
  }

  public void setAbstractMedia(byte[] abstractMedia) {
    this.abstractMedia = abstractMedia;
  }

  public String getAbstractMediaContentType() {
    return abstractMediaContentType;
  }

  public void setAbstractMediaContentType(String abstractMediaContentType) {
    this.abstractMediaContentType = abstractMediaContentType;
  }

  public ManagedContent getManagedContentId() {
    return managedContentId;
  }

  public void setManagedContentId(ManagedContent managedContentId) {
    this.managedContentId = managedContentId;
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

  public String getAbstractMediaUrl() {
    return abstractMediaUrl;
  }

  public void setAbstractMediaUrl(String abstractMediaUrl) {
    this.abstractMediaUrl = abstractMediaUrl;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM Article o", Long.class).getSingleResult();
  }

  public static List<Article> findAll() {
    return entityManager().createQuery("SELECT o FROM Article o", Article.class).getResultList();
  }

  public static Article find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(Article.class, id);
  }

  public static void publish(Integer id) {
    Article article = find(id);
    article.setStatusId(ArticleStatus.findForCd(ArticleStatus.CD_PUBLISHED));
    article.setPublishedBy(Session.getCurrentUser());
    article.setPublishedAt(Calendar.getInstance());
    article.merge();
  }

  public static List<Article> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM Article o", Article.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  public static List<Article> findForAd(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      TypedQuery<Article> query = entityManager().createQuery(
          "SELECT aa.articleId FROM AdArticle aa JOIN aa.adId a WHERE a.id = :adId", Article.class);
      query.setParameter("adId", adId);
      return query.getResultList();
    }
  }

  public static List<Article> findAvailableForAd(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      List<Article> list = new ArrayList<Article>();
      List<Article> adArticleList = Article.findForAd(adId);
      if (adArticleList.isEmpty()) {
        list = findAll();
      } else {
        TypedQuery<Article> query = entityManager().createQuery(
            "SELECT a FROM Article a WHERE a NOT IN :adArticleList", Article.class);
        query.setParameter("adArticleList", adArticleList);
        list = query.getResultList();
      }
      return list;
    }
  }

}
