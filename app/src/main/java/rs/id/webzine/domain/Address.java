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
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ADDRESS")
public class Address {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Address().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countAddresses() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Address o",
				Long.class).getSingleResult();
	}

	public static List<Address> findAllAddresses() {
		return entityManager().createQuery("SELECT o FROM Address o",
				Address.class).getResultList();
	}

	public static Address findAddress(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Address.class, id);
	}

	public static List<Address> findAddressEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Address o", Address.class)
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
			Address attached = Address.findAddress(this.id);
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
	public Address merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Address merged = this.entityManager.merge(this);
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

	@OneToMany(mappedBy = "addressId")
	private Set<Customer> customers;

	@OneToMany(mappedBy = "addressId")
	private Set<UserProfile> userProfiles;

	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
	private Country countryId;

	@Column(name = "EMAIL", length = 100)
	private String email;

	@Column(name = "PHONE", length = 50)
	private String phone;

	@Column(name = "STREET_LINE", length = 100)
	private String streetLine;

	@Column(name = "CITY", length = 50)
	private String city;

	@Column(name = "POSTAL_CODE", length = 15)
	private String postalCode;

	@Column(name = "WWW", length = 250)
	private String www;

	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public Country getCountryId() {
		return countryId;
	}

	public void setCountryId(Country countryId) {
		this.countryId = countryId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStreetLine() {
		return streetLine;
	}

	public void setStreetLine(String streetLine) {
		this.streetLine = streetLine;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}
}
