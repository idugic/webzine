package rs.id.webzine.domain;

import java.sql.Blob;
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
@Table(schema = "ADMIN", name = "USER_ARTICLE")
@Configurable
public class UserArticle {

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new UserArticle().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countUserArticles() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM UserArticle o", Long.class)
				.getSingleResult();
	}

	public static List<UserArticle> findAllUserArticles() {
		return entityManager().createQuery("SELECT o FROM UserArticle o",
				UserArticle.class).getResultList();
	}

	public static UserArticle findUserArticle(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(UserArticle.class, id);
	}

	public static List<UserArticle> findUserArticleEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM UserArticle o", UserArticle.class)
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
			UserArticle attached = UserArticle.findUserArticle(this.id);
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
	public UserArticle merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		UserArticle merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private Users um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private Users uc;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	@Column(name = "TITLE", length = 200)
	@NotNull
	private String title;

	@Column(name = "TEXT")
	private String text;

	@Column(name = "IMAGE")
	private Blob image;

	@Column(name = "DC")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dc;

	@Column(name = "DM")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dm;

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

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
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
