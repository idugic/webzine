package rs.id.webzine.domain;

import java.sql.Blob;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "ADMIN", name = "ARTICLE")
@Configurable
public class Article {

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

	@OneToMany(mappedBy = "articleId")
	private Set<Ad> ads;

	@OneToMany(mappedBy = "articleId")
	private Set<ArticleBookmark> articleBookmarks;

	@OneToMany(mappedBy = "articleId")
	private Set<ArticleCategory> articleCategories;

	@OneToMany(mappedBy = "articleId")
	private Set<ArticleComment> articleComments;

	@OneToMany(mappedBy = "articleId")
	private Set<ArticleRate> articleRates;

	@ManyToOne
	@JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
	private ManagedContent managedContentId;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private User um;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private User uc;

	@ManyToOne
	@JoinColumn(name = "AUTHOR_USER_ID", referencedColumnName = "ID", nullable = false)
	private User authorUserId;

	@ManyToOne
	@JoinColumn(name = "PUBLISHED_BY", referencedColumnName = "ID")
	private User publishedBy;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	@Column(name = "TITLE", length = 200)
	@NotNull
	private String title;

	@Column(name = "ABSTRACT_TEXT", length = 500)
	private String abstractText;

	@Column(name = "ABSTRACT_IMAGE")
	private Blob abstractImage;

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

	public Set<Ad> getAds() {
		return ads;
	}

	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

	public Set<ArticleBookmark> getArticleBookmarks() {
		return articleBookmarks;
	}

	public void setArticleBookmarks(Set<ArticleBookmark> articleBookmarks) {
		this.articleBookmarks = articleBookmarks;
	}

	public Set<ArticleCategory> getArticleCategories() {
		return articleCategories;
	}

	public void setArticleCategories(Set<ArticleCategory> articleCategories) {
		this.articleCategories = articleCategories;
	}

	public Set<ArticleComment> getArticleComments() {
		return articleComments;
	}

	public void setArticleComments(Set<ArticleComment> articleComments) {
		this.articleComments = articleComments;
	}

	public Set<ArticleRate> getArticleRates() {
		return articleRates;
	}

	public void setArticleRates(Set<ArticleRate> articleRates) {
		this.articleRates = articleRates;
	}

	public ManagedContent getManagedContentId() {
		return managedContentId;
	}

	public void setManagedContentId(ManagedContent managedContentId) {
		this.managedContentId = managedContentId;
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

	public User getAuthorUserId() {
		return authorUserId;
	}

	public void setAuthorUserId(User authorUserId) {
		this.authorUserId = authorUserId;
	}

	public User getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(User publishedBy) {
		this.publishedBy = publishedBy;
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

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public Blob getAbstractImage() {
		return abstractImage;
	}

	public void setAbstractImage(Blob abstractImage) {
		this.abstractImage = abstractImage;
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

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Article().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countArticles() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Article o",
				Long.class).getSingleResult();
	}

	public static List<Article> findAllArticles() {
		return entityManager().createQuery("SELECT o FROM Article o",
				Article.class).getResultList();
	}

	public static Article findArticle(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Article.class, id);
	}

	public static List<Article> findArticleEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Article o", Article.class)
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
			Article attached = Article.findArticle(this.id);
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
	public Article merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Article merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
