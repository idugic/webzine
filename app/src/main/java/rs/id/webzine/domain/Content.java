package rs.id.webzine.domain;

import java.sql.Blob;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "CONTENT")
public class Content {

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@OneToMany(mappedBy = "managedContentId")
	private Set<Content> contents;

	@ManyToOne
	@JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
	private Content managedContentId;

	@ManyToOne
	@JoinColumn(name = "CONTENT_TYPE_ID", referencedColumnName = "ID", nullable = false)
	private ContentType contentTypeId;

	@Column(name = "ORDER_NO", unique = true)
	@NotNull
	private Integer orderNo;

	@Column(name = "DESCRIPTION", length = 100)
	private String description;

	@Column(name = "TEXT")
	private String text;

	@Column(name = "IMAGE")
	private Blob image;

	@Column(name = "FLASH")
	private Blob flash;

	@Column(name = "HTML")
	private String html;

	@Column(name = "LINK", length = 500)
	private String link;

	@Column(name = "LINK_TARGET", length = 15)
	private String linkTarget;

	public Set<Content> getContents() {
		return contents;
	}

	public void setContents(Set<Content> contents) {
		this.contents = contents;
	}

	public Content getManagedContentId() {
		return managedContentId;
	}

	public void setManagedContentId(Content managedContentId) {
		this.managedContentId = managedContentId;
	}

	public ContentType getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(ContentType contentTypeId) {
		this.contentTypeId = contentTypeId;
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

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Blob getFlash() {
		return flash;
	}

	public void setFlash(Blob flash) {
		this.flash = flash;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Content().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countContents() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Content o",
				Long.class).getSingleResult();
	}

	public static List<Content> findAllContents() {
		return entityManager().createQuery("SELECT o FROM Content o",
				Content.class).getResultList();
	}

	public static Content findContent(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Content.class, id);
	}

	public static List<Content> findContentEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Content o", Content.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			Content attached = Content.findContent(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public Content merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Content merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
