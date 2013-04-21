package rs.id.webzine.domain.magazine;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.domain.system.User;

@Entity
@Table(schema = "ADMIN", name = "ARTICLE")
@Configurable
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private ArticleStatus status;

  @Column(name = "TITLE", length = 200)
  @NotNull
  private String title;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContent;

  @Column(name = "ABSTRACT_TEXT", length = 500)
  private String abstractText;

  @Column(name = "ABSTRACT_MEDIA")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] abstractMedia;

  @Column(name = "ABSTRACT_MEDIA_CONTENT_TYPE", length = 200)
  private String abstractMediaContentType;

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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ArticleStatus getStatus() {
    return status;
  }

  public void setStatus(ArticleStatus status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ManagedContent getManagedContent() {
    return managedContent;
  }

  public void setManagedContent(ManagedContent managedContent) {
    this.managedContent = managedContent;
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

}
