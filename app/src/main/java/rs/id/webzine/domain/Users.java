package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "USERS")
public class Users extends IdEntity {

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
	private Roles roleId;

	@ManyToOne
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", nullable = false)
	private UserStatus statusId;

	@Column(name = "USER_NAME", length = 50, unique = true)
	@NotNull
	private String userName;

	@Column(name = "PASSWORD", length = 50)
	@NotNull
	private String password;

	public Roles getRoleId() {
		return roleId;
	}

	public void setRoleId(Roles roleId) {
		this.roleId = roleId;
	}

	public UserStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(UserStatus statusId) {
		this.statusId = statusId;
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

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Users o",
				Long.class).getSingleResult();
	}

	public static List<Users> findAll() {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.getResultList();
	}

	public static Users find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Users.class, id);
	}

	public static List<Users> findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

}
