package rs.id.webzine.service.marketing;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.marketing.AdStatus;
import rs.id.webzine.service.GenericService;

@Component
public class AdStatusService extends GenericService<AdStatus> {

  public static final String CD_NEW = "new";

  public static final String CD_IN_DESIGN = "design";

  public static final String CD_READY_TO_BE_PUBLISHED = "ready";

  public static final String CD_PUBLISHED = "published";

  public static final String CD_DELETED = "deleted";

  public AdStatus findForCd(String cd) {
    AdStatus adStatus = null;

    if (cd != null) {
      TypedQuery<AdStatus> query = entityManager().createQuery("SELECT o FROM AdStatus o WHERE o.cd = :cd",
          AdStatus.class);
      query.setParameter("cd", cd);
      List<AdStatus> adStatusList = query.getResultList();
      if (!adStatusList.isEmpty()) {
        adStatus = adStatusList.get(0);
      }
    }

    return adStatus;
  }

}
