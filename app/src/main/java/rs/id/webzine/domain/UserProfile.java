package rs.id.webzine.domain;

import java.sql.Blob;
import java.util.Date;
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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "USER_PROFILE")
public class UserProfile {

	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
	private Address addressId;

	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
	private Users userId;

	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@Column(name = "BIRTHDAY")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date birthday;

	@Column(name = "IMAGE")
	private Blob image;

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public Users getUserId() {
		return userId;
	}

	public void setUserId(Users userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Blob getImage() {
		return image;
	}

	public void setImage(Blob image) {
		this.image = image;
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

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new UserProfile().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countUserProfiles() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM UserProfile o", Long.class)
				.getSingleResult();
	}

	public static List<UserProfile> findAllUserProfiles() {
		return entityManager().createQuery("SELECT o FROM UserProfile o",
				UserProfile.class).getResultList();
	}

	public static UserProfile findUserProfile(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(UserProfile.class, id);
	}

	public static List<UserProfile> findUserProfileEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM UserProfile o", UserProfile.class)
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
			UserProfile attached = UserProfile.findUserProfile(this.id);
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
	public UserProfile merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		UserProfile merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
