package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "ADDRESS")
public class Address extends IdEntity {

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

	@Column(name = "COUNTRY", length = 50)
	private String country;

	@Column(name = "COUNTRY_CODE", length = 15)
	private String countryCode;

	@Column(name = "WWW", length = 250)
	private String www;

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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Address o",
				Long.class).getSingleResult();
	}

	public static List<Address> findAll() {
		return entityManager().createQuery("SELECT o FROM Address o",
				Address.class).getResultList();
	}

	public static Address find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Address.class, id);
	}

	public static List<Address> findEntries(int firstResult, int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM Address o", Address.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

}
