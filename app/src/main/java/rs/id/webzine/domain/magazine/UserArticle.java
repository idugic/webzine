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

import rs.id.webzine.domain.system.User;

@Entity
@Table(schema = "ADMIN", name = "USER_ARTICLE")
@Configurable
public class UserArticle {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private UserArticleStatus status;

  @Column(name = "TITLE", length = 200)
  @NotNull
  private String title;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "MEDIA")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] media;

  @Column(name = "MEDIA_CONTENT_TYPE")
  private String mediaContentType;

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

  public UserArticleStatus getStatus() {
    return status;
  }

  public void setStatus(UserArticleStatus status) {
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public byte[] getMedia() {
    return media;
  }

  public void setMedia(byte[] media) {
    this.media = media;
  }

  public String getMediaContentType() {
    return mediaContentType;
  }

  public void setMediaContentType(String mediaContentType) {
    this.mediaContentType = mediaContentType;
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
