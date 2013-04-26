package rs.id.webzine.domain.marketing;

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

import rs.id.webzine.domain.IdEntity;
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.system.User;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "AD_ARTICLE")
public class AdArticle extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "AD_ID", referencedColumnName = "ID")
  private Ad adId;

  @ManyToOne
  @JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
  private Article articleId;

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

  public Ad getAdId() {
    return adId;
  }

  public void setAdId(Ad adId) {
    this.adId = adId;
  }

  public Article getArticleId() {
    return articleId;
  }

  public void setArticleId(Article articleId) {
    this.articleId = articleId;
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
    return entityManager().createQuery("SELECT COUNT(o) FROM AdArticle o", Long.class).getSingleResult();
  }

  public static List<AdArticle> findAll() {
    return entityManager().createQuery("SELECT o FROM AdArticle o", AdArticle.class).getResultList();
  }

  public static AdArticle find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(AdArticle.class, id);
  }

  public static List<AdArticle> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM AdArticle o", AdArticle.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  public static List<AdArticle> findForAd(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      TypedQuery<AdArticle> query = entityManager().createQuery(
          "SELECT aa FROM AdArticle aa JOIN aa.adId a WHERE a.id = :adId", AdArticle.class);
      query.setParameter("adId", adId);
      return query.getResultList();
    }
  }
}
