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

@Entity
@Table(schema = "ADMIN", name = "CUSTOMER")
@Configurable
public class Customer {

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@OneToMany(mappedBy = "customerId")
	private Set<Ad> ads;

	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
	private Address addressId;

	@Column(name = "NAME", length = 100)
	@NotNull
	private String name;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	public Set<Ad> getAds() {
		return ads;
	}

	public void setAds(Set<Ad> ads) {
		this.ads = ads;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		EntityManager em = new Customer().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countCustomers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Customer o",
				Long.class).getSingleResult();
	}

	public static List<Customer> findAllCustomers() {
		return entityManager().createQuery("SELECT o FROM Customer o",
				Customer.class).getResultList();
	}

	public static Customer findCustomer(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Customer.class, id);
	}

	public static List<Customer> findCustomerEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Customer o", Customer.class)
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
			Customer attached = Customer.findCustomer(this.id);
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
	public Customer merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Customer merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
