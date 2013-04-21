package rs.id.webzine.service.magazine;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.magazine.ArticleCommentStatus;
import rs.id.webzine.service.GenericService;

@Component
public class ArticleCommentStatusService extends GenericService<ArticleCommentStatus> {

  public static final String CD_NEW = "new";

  public static final String CD_READY_TO_BE_PUBLISHED = "ready";

  public static final String CD_PUBLISHED = "published";

  public static final String CD_DELETED = "deleted";

  public ArticleCommentStatus findForCd(String cd) {
    ArticleCommentStatus articleCommentStatus = null;

    if (cd != null) {
      TypedQuery<ArticleCommentStatus> query = entityManager().createQuery(
          "SELECT o FROM ArticleCommentStatus o WHERE o.cd = :cd", ArticleCommentStatus.class);
      query.setParameter("cd", cd);
      List<ArticleCommentStatus> articleCommentStatusList = query.getResultList();
      if (!articleCommentStatusList.isEmpty()) {
        articleCommentStatus = articleCommentStatusList.get(0);
      }
    }

    return articleCommentStatus;
  }
}
