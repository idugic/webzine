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
@Table(schema = "ADMIN", name = "ARTICLE_RATE")
@Configurable
public class ArticleRate {

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

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ArticleRate().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countArticleRates() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM ArticleRate o", Long.class)
				.getSingleResult();
	}

	public static List<ArticleRate> findAllArticleRates() {
		return entityManager().createQuery("SELECT o FROM ArticleRate o",
				ArticleRate.class).getResultList();
	}

	public static ArticleRate findArticleRate(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(ArticleRate.class, id);
	}

	public static List<ArticleRate> findArticleRateEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ArticleRate o", ArticleRate.class)
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
			ArticleRate attached = ArticleRate.findArticleRate(this.id);
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
	public ArticleRate merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ArticleRate merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@ManyToOne
	@JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
	private Article articleId;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private User um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private User uc;

	@Column(name = "RATE")
	@NotNull
	private Integer rate;

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

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
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
