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
@Table(schema = "ADMIN", name = "ARTICLE_COMMENT")
public class ArticleComment {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ArticleComment().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countArticleComments() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM ArticleComment o", Long.class)
				.getSingleResult();
	}

	public static List<ArticleComment> findAllArticleComments() {
		return entityManager().createQuery("SELECT o FROM ArticleComment o",
				ArticleComment.class).getResultList();
	}

	public static ArticleComment findArticleComment(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(ArticleComment.class, id);
	}

	public static List<ArticleComment> findArticleCommentEntries(
			int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ArticleComment o",
						ArticleComment.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
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
			ArticleComment attached = ArticleComment
					.findArticleComment(this.id);
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
	public ArticleComment merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ArticleComment merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
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

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@ManyToOne
	@JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
	private Article articleId;

	@ManyToOne
	@JoinColumn(name = "PUBLISHED_BY", referencedColumnName = "ID")
	private User publishedBy;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private User um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private User uc;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	@Column(name = "TEXT", length = 250)
	@NotNull
	private String text;

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

	public Article getArticleId() {
		return articleId;
	}

	public void setArticleId(Article articleId) {
		this.articleId = articleId;
	}

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
}
