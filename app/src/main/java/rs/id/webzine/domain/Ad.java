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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

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

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContentId;

  @ManyToOne
  @JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID")
  private Article articleId;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "VALID_FROM")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar validFrom;

  @Column(name = "VALID_TO")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar validTo;

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

  public Article getArticleId() {
    return articleId;
  }

  public void setArticleId(Article articleId) {
    this.articleId = articleId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Calendar getValidFrom() {
    return validFrom;
  }

  public void setValidFrom(Calendar validFrom) {
    this.validFrom = validFrom;
  }

  public Calendar getValidTo() {
    return validTo;
  }

  public void setValidTo(Calendar validTo) {
    this.validTo = validTo;
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

}
