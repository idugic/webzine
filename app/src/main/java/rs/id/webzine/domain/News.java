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

@Configurable
@Entity
@Table(schema = "ADMIN", name = "NEWS")
public class News extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "PUBLISHED_BY", referencedColumnName = "ID")
  private User publishedBy;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  private User uc;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private NewsStatus statusId;

  @Column(name = "TEXT", length = 100)
  @NotNull
  private String text;

  @Column(name = "LINK", length = 250)
  private String link;

  @Column(name = "LINK_TARGET", length = 15)
  private String linkTarget;

  @Column(name = "PUBLISHED_AT")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar publishedAt;

  @Column(name = "DC")
  @NotNull
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dc;

  @Column(name = "DM")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dm;

  public User getPublishedBy() {
    return publishedBy;
  }

  public void setPublishedBy(User publishedBy) {
    this.publishedBy = publishedBy;
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

  public NewsStatus getStatusId() {
    return statusId;
  }

  public void setStatusId(NewsStatus statusId) {
    this.statusId = statusId;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLinkTarget() {
    return linkTarget;
  }

  public void setLinkTarget(String linkTarget) {
    this.linkTarget = linkTarget;
  }

  public Calendar getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(Calendar publishedAt) {
    this.publishedAt = publishedAt;
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
    return entityManager().createQuery("SELECT COUNT(o) FROM News o", Long.class).getSingleResult();
  }

  public static List<News> findAll() {
    return entityManager().createQuery("SELECT o FROM News o", News.class).getResultList();
  }

  public static News find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(News.class, id);
  }

  public static List<News> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM News o", News.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }
}
