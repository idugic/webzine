package rs.id.webzine.domain;

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
@Table(schema = "ADMIN", name = "USERS")
public class Users {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Users().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countUserses() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Users o",
				Long.class).getSingleResult();
	}

	public static List<Users> findAllUserses() {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.getResultList();
	}

	public static Users findUsers(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Users.class, id);
	}

	public static List<Users> findUsersEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
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
			Users attached = Users.findUsers(this.id);
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
	public Users merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Users merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@OneToMany(mappedBy = "publishedBy")
	private Set<Article> articles;

	@OneToMany(mappedBy = "authorUserId")
	private Set<Article> articles1;

	@OneToMany(mappedBy = "uc")
	private Set<Article> articles2;

	@OneToMany(mappedBy = "um")
	private Set<Article> articles3;

	@OneToMany(mappedBy = "uc")
	private Set<ArticleBookmark> articleBookmarks;

	@OneToMany(mappedBy = "um")
	private Set<ArticleBookmark> articleBookmarks1;

	@OneToMany(mappedBy = "uc")
	private Set<ArticleCategory> articleCategories;

	@OneToMany(mappedBy = "um")
	private Set<ArticleCategory> articleCategories1;

	@OneToMany(mappedBy = "uc")
	private Set<ArticleComment> articleComments;

	@OneToMany(mappedBy = "um")
	private Set<ArticleComment> articleComments1;

	@OneToMany(mappedBy = "publishedBy")
	private Set<ArticleComment> articleComments2;

	@OneToMany(mappedBy = "uc")
	private Set<ArticleRate> articleRates;

	@OneToMany(mappedBy = "um")
	private Set<ArticleRate> articleRates1;

	@OneToMany(mappedBy = "uc")
	private Set<Category> categories;

	@OneToMany(mappedBy = "um")
	private Set<Category> categories1;

	@OneToMany(mappedBy = "uc")
	private Set<News> news;

	@OneToMany(mappedBy = "um")
	private Set<News> news1;

	@OneToMany(mappedBy = "publishedBy")
	private Set<News> news2;

	@OneToMany(mappedBy = "um")
	private Set<Task> tasks;

	@OneToMany(mappedBy = "ownerUserId")
	private Set<Task> tasks1;

	@OneToMany(mappedBy = "uc")
	private Set<Task> tasks2;

	@OneToMany(mappedBy = "uc")
	private Set<TaskAttachment> taskAttachments;

	@OneToMany(mappedBy = "um")
	private Set<TaskAttachment> taskAttachments1;

	@OneToMany(mappedBy = "um")
	private Set<TaskComment> taskComments;

	@OneToMany(mappedBy = "uc")
	private Set<TaskComment> taskComments1;

	@OneToMany(mappedBy = "uc")
	private Set<UserArticle> userArticles;

	@OneToMany(mappedBy = "um")
	private Set<UserArticle> userArticles1;

	@OneToMany(mappedBy = "userId")
	private Set<UserProfile> userProfiles;

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
	private Roles roleId;

	@Column(name = "USER_NAME", length = 50, unique = true)
	@NotNull
	private String userName;

	@Column(name = "PASSWORD", length = 50)
	@NotNull
	private String password;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public Set<Article> getArticles1() {
		return articles1;
	}

	public void setArticles1(Set<Article> articles1) {
		this.articles1 = articles1;
	}

	public Set<Article> getArticles2() {
		return articles2;
	}

	public void setArticles2(Set<Article> articles2) {
		this.articles2 = articles2;
	}

	public Set<Article> getArticles3() {
		return articles3;
	}

	public void setArticles3(Set<Article> articles3) {
		this.articles3 = articles3;
	}

	public Set<ArticleBookmark> getArticleBookmarks() {
		return articleBookmarks;
	}

	public void setArticleBookmarks(Set<ArticleBookmark> articleBookmarks) {
		this.articleBookmarks = articleBookmarks;
	}

	public Set<ArticleBookmark> getArticleBookmarks1() {
		return articleBookmarks1;
	}

	public void setArticleBookmarks1(Set<ArticleBookmark> articleBookmarks1) {
		this.articleBookmarks1 = articleBookmarks1;
	}

	public Set<ArticleCategory> getArticleCategories() {
		return articleCategories;
	}

	public void setArticleCategories(Set<ArticleCategory> articleCategories) {
		this.articleCategories = articleCategories;
	}

	public Set<ArticleCategory> getArticleCategories1() {
		return articleCategories1;
	}

	public void setArticleCategories1(Set<ArticleCategory> articleCategories1) {
		this.articleCategories1 = articleCategories1;
	}

	public Set<ArticleComment> getArticleComments() {
		return articleComments;
	}

	public void setArticleComments(Set<ArticleComment> articleComments) {
		this.articleComments = articleComments;
	}

	public Set<ArticleComment> getArticleComments1() {
		return articleComments1;
	}

	public void setArticleComments1(Set<ArticleComment> articleComments1) {
		this.articleComments1 = articleComments1;
	}

	public Set<ArticleComment> getArticleComments2() {
		return articleComments2;
	}

	public void setArticleComments2(Set<ArticleComment> articleComments2) {
		this.articleComments2 = articleComments2;
	}

	public Set<ArticleRate> getArticleRates() {
		return articleRates;
	}

	public void setArticleRates(Set<ArticleRate> articleRates) {
		this.articleRates = articleRates;
	}

	public Set<ArticleRate> getArticleRates1() {
		return articleRates1;
	}

	public void setArticleRates1(Set<ArticleRate> articleRates1) {
		this.articleRates1 = articleRates1;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Set<Category> getCategories1() {
		return categories1;
	}

	public void setCategories1(Set<Category> categories1) {
		this.categories1 = categories1;
	}

	public Set<News> getNews() {
		return news;
	}

	public void setNews(Set<News> news) {
		this.news = news;
	}

	public Set<News> getNews1() {
		return news1;
	}

	public void setNews1(Set<News> news1) {
		this.news1 = news1;
	}

	public Set<News> getNews2() {
		return news2;
	}

	public void setNews2(Set<News> news2) {
		this.news2 = news2;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<Task> getTasks1() {
		return tasks1;
	}

	public void setTasks1(Set<Task> tasks1) {
		this.tasks1 = tasks1;
	}

	public Set<Task> getTasks2() {
		return tasks2;
	}

	public void setTasks2(Set<Task> tasks2) {
		this.tasks2 = tasks2;
	}

	public Set<TaskAttachment> getTaskAttachments() {
		return taskAttachments;
	}

	public void setTaskAttachments(Set<TaskAttachment> taskAttachments) {
		this.taskAttachments = taskAttachments;
	}

	public Set<TaskAttachment> getTaskAttachments1() {
		return taskAttachments1;
	}

	public void setTaskAttachments1(Set<TaskAttachment> taskAttachments1) {
		this.taskAttachments1 = taskAttachments1;
	}

	public Set<TaskComment> getTaskComments() {
		return taskComments;
	}

	public void setTaskComments(Set<TaskComment> taskComments) {
		this.taskComments = taskComments;
	}

	public Set<TaskComment> getTaskComments1() {
		return taskComments1;
	}

	public void setTaskComments1(Set<TaskComment> taskComments1) {
		this.taskComments1 = taskComments1;
	}

	public Set<UserArticle> getUserArticles() {
		return userArticles;
	}

	public void setUserArticles(Set<UserArticle> userArticles) {
		this.userArticles = userArticles;
	}

	public Set<UserArticle> getUserArticles1() {
		return userArticles1;
	}

	public void setUserArticles1(Set<UserArticle> userArticles1) {
		this.userArticles1 = userArticles1;
	}

	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public Roles getRoleId() {
		return roleId;
	}

	public void setRoleId(Roles roleId) {
		this.roleId = roleId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
