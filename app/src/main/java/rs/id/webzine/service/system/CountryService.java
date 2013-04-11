package rs.id.webzine.service.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.system.Country;
import rs.id.webzine.service.Service;

@Component
public class CountryService extends Service {

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM Country o", Long.class).getSingleResult();
  }

  public List<Country> findAll() {
    return entityManager().createQuery("SELECT o FROM Country o", Country.class).getResultList();
  }

  public Country find(Integer id) {
    return entityManager().find(Country.class, id);
  }

  public List<Country> findForList(int firstResult, int maxResults) {
    List<Country> countryList = new ArrayList<Country>();
    TypedQuery<Country> query = entityManager().createQuery("SELECT o FROM Country o", Country.class)
        .setFirstResult(firstResult).setMaxResults(maxResults);
    countryList = query.getResultList();
    return countryList;
  }

  public Country findForCd(String cd) {
    Country country = null;

    if (cd != null) {
      TypedQuery<Country> query = entityManager().createQuery("SELECT o FROM Country o WHERE cd = :cd", Country.class);
      query.setParameter("cd", cd);
      List<Country> countryList = query.getResultList();
      if (!countryList.isEmpty()) {
        country = countryList.get(0);
      }
    }

    return country;
  }
}
