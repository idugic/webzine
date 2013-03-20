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
@Table(schema = "ADMIN", name = "ARTICLE_CATEGORY")
public class ArticleCategory {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ArticleCategory().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countArticleCategorys() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM ArticleCategory o", Long.class)
				.getSingleResult();
	}

	public static List<ArticleCategory> findAllArticleCategorys() {
		return entityManager().createQuery("SELECT o FROM ArticleCategory o",
				ArticleCategory.class).getResultList();
	}

	public static ArticleCategory findArticleCategory(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(ArticleCategory.class, id);
	}

	public static List<ArticleCategory> findArticleCategoryEntries(
			int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ArticleCategory o",
						ArticleCategory.class).setFirstResult(firstResult)
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
			ArticleCategory attached = ArticleCategory
					.findArticleCategory(this.id);
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
	public ArticleCategory merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ArticleCategory merged = this.entityManager.merge(this);
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

	@ManyToOne
	@JoinColumn(name = "ARTICLE_ID", referencedColumnName = "ID", nullable = false)
	private Article articleId;

	@ManyToOne
	@JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
	private Category categoryId;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private Users um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private Users uc;

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

	public Category getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Category categoryId) {
		this.categoryId = categoryId;
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
