package rs.id.webzine.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.system.Country;
import rs.id.webzine.service.GenericService;

@Component
public class CountryService extends GenericService<Country> {

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
