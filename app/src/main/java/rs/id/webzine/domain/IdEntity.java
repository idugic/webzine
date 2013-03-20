package rs.id.webzine.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@MappedSuperclass
public class IdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new IdEntity().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected.");
		return em;
	}

	@Transactional
	public void persist() {
		entityManager().persist(this);
	}

	@Transactional
	public void remove() {
		if (entityManager().contains(this)) {
			entityManager().remove(this);
		} else {
			IdEntity attached = entityManager().find(getClass(), id);
			entityManager().remove(attached);
		}
	}

	@Transactional
	public void flush() {
		entityManager().flush();
	}

	@Transactional
	public void clear() {
		entityManager().clear();
	}

	@Transactional
	public IdEntity merge() {
		IdEntity abstractEntity = entityManager().merge(this);
		entityManager().flush();
		return abstractEntity;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
