package rs.id.webzine.domain.content_management;

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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "CONTENT")
public class Content {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "CONTENT_TYPE_ID", referencedColumnName = "ID", nullable = false)
  private ContentType contentType;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContent;

  @Column(name = "ORDER_NO")
  @NotNull
  private Integer orderNo;

  @Column(name = "DESCRIPTION", length = 100)
  @NotNull
  private String description;

  @Column(name = "TEXT")
  private String text;

  @Column(name = "MEDIA")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] media;

  @Column(name = "MEDIA_CONTENT_TYPE", length = 100)
  private String mediaContentType;

  @Column(name = "LINK", length = 500)
  private String link;

  @Column(name = "LINK_TARGET", length = 15)
  private String linkTarget;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ContentType getContentType() {
    return contentType;
  }

  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  public ManagedContent getManagedContent() {
    return managedContent;
  }

  public void setManagedContent(ManagedContent managedContent) {
    this.managedContent = managedContent;
  }

  public Integer getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(Integer orderNo) {
    this.orderNo = orderNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

}
