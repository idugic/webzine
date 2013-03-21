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

	public static long countUsers() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Users o",
				Long.class).getSingleResult();
	}

	public static List<Users> findAllUsers() {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.getResultList();
	}

	public static Users findUser(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Users.class, id);
	}

	public static List<Users> findUserEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Users o", Users.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

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

}
