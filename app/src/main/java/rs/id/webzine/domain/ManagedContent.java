package rs.id.webzine.domain;

import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(schema = "ADMIN", name = "MANAGED_CONTENT")
@Configurable
public class ManagedContent {

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

	@OneToMany(mappedBy = "managedContentId")
	private Set<Ad> ads;

	@OneToMany(mappedBy = "managedContentId")
	private Set<Article> articles;

	@Column(name = "CSS")
	private String css;

	public Set<Ad> getAds() {
		return ads;
	}

	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

	public Set<Article> getArticles() {
		return articles;
	}

	public void setArticles(Set<Article> articles) {
		this.articles = articles;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ManagedContent().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countManagedContents() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM ManagedContent o", Long.class)
				.getSingleResult();
	}

	public static List<ManagedContent> findAllManagedContents() {
		return entityManager().createQuery("SELECT o FROM ManagedContent o",
				ManagedContent.class).getResultList();
	}

	public static ManagedContent findManagedContent(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(ManagedContent.class, id);
	}

	public static List<ManagedContent> findManagedContentEntries(
			int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM ManagedContent o",
						ManagedContent.class).setFirstResult(firstResult)
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
			ManagedContent attached = ManagedContent
					.findManagedContent(this.id);
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
	public ManagedContent merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ManagedContent merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
