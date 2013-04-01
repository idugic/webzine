package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "CONTENT")
public class Content extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "CONTENT_TYPE_ID", referencedColumnName = "ID", nullable = false)
  private ContentType contentTypeId;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContentId;

  @Column(name = "ORDER_NO")
  @NotNull
  private Integer orderNo;

  @Column(name = "DESCRIPTION", length = 100)
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

  public ContentType getContentTypeId() {
    return contentTypeId;
  }

  public void setContentTypeId(ContentType contentTypeId) {
    this.contentTypeId = contentTypeId;
  }

  public ManagedContent getManagedContentId() {
    return managedContentId;
  }

  public void setManagedContentId(ManagedContent managedContentId) {
    this.managedContentId = managedContentId;
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

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM Content o", Long.class).getSingleResult();
  }

  public static List<Content> findAll() {
    return entityManager().createQuery("SELECT o FROM Content o", Content.class).getResultList();
  }

  public static Content find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(Content.class, id);
  }

  public static List<Content> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM Content o", Content.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }
}
