package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ROLES")
public class Roles extends IdEntity {

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

	public static long countRoleses() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Roles o",
				Long.class).getSingleResult();
	}

	public static List<Roles> findAllRoleses() {
		return entityManager()
				.createQuery("SELECT o FROM Roles o", Roles.class)
				.getResultList();
	}

	public static Roles findRoles(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Roles.class, id);
	}

	public static List<Roles> findRolesEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Roles o", Roles.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}
}
