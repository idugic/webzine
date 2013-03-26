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
public class Role extends IdEntity {

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
		return entityManager().createQuery("SELECT COUNT(o) FROM Role o", Long.class).getSingleResult();
	}

	public static List<Role> findAll() {
		return entityManager().createQuery("SELECT o FROM Role o", Role.class).getResultList();
	}

	public static Role find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Role.class, id);
	}

	public static List<Role> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Role o", Role.class).setFirstResult(firstResult)
		        .setMaxResults(maxResults).getResultList();
	}
}
