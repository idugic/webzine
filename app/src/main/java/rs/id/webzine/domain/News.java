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

@Configurable
@Entity
@Table(schema = "ADMIN", name = "NEWS")
public class News {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new News().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countNews() {
		return entityManager().createQuery("SELECT COUNT(o) FROM News o",
				Long.class).getSingleResult();
	}

	public static List<News> findAllNews() {
		return entityManager().createQuery("SELECT o FROM News o", News.class)
				.getResultList();
	}

	public static News findNews(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(News.class, id);
	}

	public static List<News> findNewsEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM News o", News.class)
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
			News attached = News.findNews(this.id);
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
	public News merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		News merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@ManyToOne
	@JoinColumn(name = "PUBLISHED_BY", referencedColumnName = "ID")
	private Users publishedBy;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private Users um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private Users uc;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

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

	public Users getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(Users publishedBy) {
		this.publishedBy = publishedBy;
	}

	public Users getUm() {
		return um;
	}

	public void setUm(Users um) {
		this.um = um;
	}

	public Users getUc() {
		return uc;
	}

	public void setUc(Users uc) {
		this.uc = uc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
