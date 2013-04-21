package rs.id.webzine.domain;

import java.util.ArrayList;
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

import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.magazine.ArticleService;

@Entity
@Table(schema = "ADMIN", name = "AD")
@Configurable
public class Ad extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private AdStatus statusId;

  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", nullable = false)
  private Customer customerId;

  @Column(name = "NAME", length = 100)
  @NotNull
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContentId;

  @Column(name = "VALID_FROM")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar validFrom;

  @Column(name = "VALID_UNTIL")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar validUntil;

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

  public AdStatus getStatusId() {
    return statusId;
  }

  public void setStatusId(AdStatus statusId) {
    this.statusId = statusId;
  }

  public Customer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Customer customerId) {
    this.customerId = customerId;
  }

  public ManagedContent getManagedContentId() {
    return managedContentId;
  }

  public void setManagedContentId(ManagedContent managedContentId) {
    this.managedContentId = managedContentId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Calendar getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Calendar validFrom) {
    this.validFrom = validFrom;
  }

  public Calendar getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(Calendar validUntil) {
    this.validUntil = validUntil;
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
    return entityManager().createQuery("SELECT COUNT(o) FROM Ad o", Long.class).getSingleResult();
  }

  public static List<Ad> findAll() {
    return entityManager().createQuery("SELECT o FROM Ad o", Ad.class).getResultList();
  }

  public static Ad find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(Ad.class, id);
  }

  public static List<Ad> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM Ad o", Ad.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  public static List<ArticleService> findArticles(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      TypedQuery<ArticleService> query = entityManager().createQuery(
          "SELECT aa.articleId FROM AdArticle aa JOIN aa.adId a WHERE a.id = :adId", ArticleService.class);
      query.setParameter("adId", adId);
      return query.getResultList();
    }
  }

  // public static List<ArticleService> findAvailableArticles(Integer adId) {
  // if (adId == null) {
  // return null;
  // } else {
  // List<ArticleService> list = new ArrayList<ArticleService>();
  // List<ArticleService> adArticleList = ArticleService.findForAd(adId);
  // if (adArticleList.isEmpty()) {
  // list = findAll();
  // } else {
  // TypedQuery<ArticleService> query = entityManager().createQuery(
  // "SELECT a FROM Article a WHERE a NOT IN :adArticleList",
  // ArticleService.class);
  // query.setParameter("adArticleList", adArticleList);
  // list = query.getResultList();
  // }
  // return list;
  // }
  // }
  
}
