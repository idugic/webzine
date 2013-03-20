package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "ADMIN", name = "AD")
@Configurable
public class Ad {

	@ManyToOne
	@JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID")
	private Article articleId;

	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID", nullable = false)
	private Customer customerId;

	@ManyToOne
	@JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
	private ManagedContent managedContentId;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

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

	public Article getArticleId() {
		return articleId;
	}

	public void setArticleId(Article articleId) {
		this.articleId = articleId;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Ad().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAds() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Ad o",
				Long.class).getSingleResult();
	}

	public static List<Ad> findAllAds() {
		return entityManager().createQuery("SELECT o FROM Ad o", Ad.class)
				.getResultList();
	}

	public static Ad findAd(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Ad.class, id);
	}

	public static List<Ad> findAdEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Ad o", Ad.class)
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
			Ad attached = Ad.findAd(this.id);
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
	public Ad merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Ad merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
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
}
