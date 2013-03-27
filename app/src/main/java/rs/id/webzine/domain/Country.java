package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NoResultException;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "COUNTRY")
public class Country extends IdEntity {
	private static Log log = LogFactory.getLog(Country.class);

	@Column(name = "CD", length = 15, unique = true)
	@NotNull
	private String cd;

	@Column(name = "NAME", length = 50)
	@NotNull
	private String name;

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Country o", Long.class).getSingleResult();
	}

	public static List<Country> findAll() {
		return entityManager().createQuery("SELECT o FROM Country o", Country.class).getResultList();
	}

	public static Country find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Country.class, id);
	}

	public static List<Country> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Country o", Country.class).setFirstResult(firstResult)
		        .setMaxResults(maxResults).getResultList();
	}

	public static Country findForCd(String cd) {
		Country country = null;

		try {
			if (cd == null) {
				TypedQuery<Country> query = entityManager().createQuery("SELECT o FROM Country o WHERE cd = :cd",
				        Country.class);
				query.setParameter("cd", cd);
				country = query.getSingleResult();
			}
		} catch (NoResultException e) {
			log.debug(e);
		}

		return country;
	}
}
