package rs.id.webzine.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "USERS")
public class User extends IdEntity {

	private static Log log = LogFactory.getLog(User.class);

	@ManyToOne
	@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID", nullable = false)
	private Role roleId;

	@ManyToOne
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", nullable = false)
	private UserStatus statusId;

	@Column(name = "USER_NAME", length = 50, unique = true)
	@NotNull
	private String userName;

	@Column(name = "PASSWORD", length = 250)
	@NotNull
	private String password;

	@Column(name = "FIRST_NAME", length = 50)
	private String firstName;

	@Column(name = "LAST_NAME", length = 50)
	private String lastName;

	@Column(name = "BIRTHDATE")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(style = "M-")
	private Date birthdate;

	@Column(name = "IMAGE")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] image;

	@Column(name = "IMAGE_CONTENT_TYPE", length = 100)
	private String imageContentType;

	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
	private Address addressId;

	public Role getRoleId() {
		return roleId;
	}

	public void setRoleId(Role roleId) {
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

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageContentType() {
		return imageContentType;
	}

	public void setImageContentType(String imageContentType) {
		this.imageContentType = imageContentType;
	}

	public Address getAddressId() {
		return addressId;
	}

	public void setAddressId(Address addressId) {
		this.addressId = addressId;
	}

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM User o", Long.class).getSingleResult();
	}

	public static List<User> findAll() {
		return entityManager().createQuery("SELECT o FROM User o", User.class).getResultList();
	}

	public static User find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(User.class, id);
	}

	public static List<User> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM User o", User.class).setFirstResult(firstResult)
		        .setMaxResults(maxResults).getResultList();
	}

	public static User findForUserName(String userName) {
		User user = null;

		try {
			if (userName != null) {
				TypedQuery<User> query = entityManager().createQuery("SELECT o FROM User o WHERE userName = :userName",
				        User.class);
				query.setParameter("userName", userName);
				user = query.getSingleResult();
			}
		} catch (Exception e) {
			log.debug(e);
		}

		return user;
	}
}
