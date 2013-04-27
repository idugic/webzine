package rs.id.webzine.service.magazine;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.magazine.ArticleStatus;
import rs.id.webzine.service.GenericService;

@Component
public class ArticleStatusService extends GenericService<ArticleStatus> {

  public static final String CD_NEW = "new";

  public static final String CD_IN_DESIGN = "design";

  public static final String CD_READY_TO_BE_PUBLISHED = "ready";

  public static final String CD_PUBLISHED = "published";

  public static final String CD_DELETED = "deleted";

  public ArticleStatus findForCd(String cd) {
    ArticleStatus articleStatus = null;

    if (cd != null) {
      TypedQuery<ArticleStatus> query = entityManager().createQuery("SELECT o FROM ArticleStatus o WHERE o.cd = :cd",
          ArticleStatus.class);
      query.setParameter("cd", cd);
      List<ArticleStatus> articleStatusList = query.getResultList();
      if (!articleStatusList.isEmpty()) {
        articleStatus = articleStatusList.get(0);
      }
    }

    return articleStatus;
  }
}
